package com.pscheol.ionicsample.plugins

import android.content.ContentUris
import android.graphics.Bitmap
import android.graphics.Matrix
import java.io.ByteArrayOutputStream
import java.io.File
import android.provider.MediaStore
import android.content.Context
import android.database.Cursor
import android.database.Cursor.FIELD_TYPE_INTEGER
import android.graphics.ImageDecoder
import android.os.AsyncTask
import android.os.Build
import android.util.Size
import java.io.FileOutputStream

/** PhotoGalleryPlugin */
class PhotoGallery {

    private val context: Context

    constructor(context: Context) {
        this.context = context
    }

    companion object {
        const val imageType = "image"
        const val videoType = "video"

        const val allAlbumId = "__ALL__"
        const val allAlbumName = "All"

        val imageMetadataProjection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_TAKEN,
            MediaStore.Images.Media.DATE_MODIFIED
        )

        val videoMetadataProjection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.WIDTH,
            MediaStore.Video.Media.HEIGHT,
            MediaStore.Video.Media.MIME_TYPE,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.DATE_TAKEN,
            MediaStore.Video.Media.DATE_MODIFIED
        )

        const val imageOrderBy = "${MediaStore.Images.Media.DATE_TAKEN} DESC, ${MediaStore.Images.Media.DATE_MODIFIED} DESC"
        const val videoOrderBy = "${MediaStore.Video.Media.DATE_TAKEN} DESC, ${MediaStore.Video.Media.DATE_MODIFIED} DESC"
    }

    fun listAlbums(mediumType: String): List<Map<String, Any>> {
        return when (mediumType) {
            imageType -> {
                listImageAlbums()
            }
            videoType -> {
                listVideoAlbums()
            }
            else -> {
                listOf()
            }
        }
    }

    fun listImageAlbums(): List<Map<String, Any>> {
        this.context?.run {
            var total = 0
            val albumHashMap = mutableMapOf<String, MutableMap<String, Any>>()

            val imageProjection = arrayOf(
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_ID
            )

            val imageCursor = this.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageProjection,
                null,
                null,
                null
            )

            imageCursor?.use { cursor ->
                val bucketColumn = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
                val bucketColumnId = cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID)

                while (cursor.moveToNext()) {
                    val bucketId = cursor.getString(bucketColumnId)
                    val album = albumHashMap[bucketId]
                    if (album == null) {
                        val folderName = cursor.getString(bucketColumn)
                        albumHashMap[bucketId] = mutableMapOf(
                            "id" to bucketId,
                            "mediumType" to imageType,
                            "name" to folderName,
                            "count" to 1
                        )
                    } else {
                        val count = album["count"] as Int
                        album["count"] = count + 1
                    }
                    total++
                }
            }

            val albumList = mutableListOf<Map<String, Any>>()
            albumList.add(
                mapOf(
                    "id" to allAlbumId,
                    "mediumType" to imageType,
                    "name" to allAlbumName,
                    "count" to total
                )
            )
            albumList.addAll(albumHashMap.values)
            return albumList
        }
        return listOf()
    }

    fun listVideoAlbums(): List<Map<String, Any>> {
        this.context?.run {
            var total = 0
            val albumHashMap = mutableMapOf<String, MutableMap<String, Any>>()

            val videoProjection = arrayOf(
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_ID
            )

            val videoCursor = this.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoProjection,
                null,
                null,
                null
            )

            videoCursor?.use { cursor ->
                val bucketColumn = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
                val bucketColumnId = cursor.getColumnIndex(MediaStore.Video.Media.BUCKET_ID)

                while (cursor.moveToNext()) {
                    val bucketId = cursor.getString(bucketColumnId)
                    val album = albumHashMap[bucketId]
                    if (album == null) {
                        val folderName = cursor.getString(bucketColumn)
                        albumHashMap[bucketId] = mutableMapOf(
                            "id" to bucketId,
                            "mediumType" to videoType,
                            "name" to folderName,
                            "count" to 1
                        )
                    } else {
                        val count = album["count"] as Int
                        album["count"] = count + 1
                    }
                    total++
                }
            }

            val albumList = mutableListOf<Map<String, Any>>()
            albumList.add(mapOf(
                "id" to allAlbumId,
                "mediumType" to videoType,
                "name" to allAlbumName,
                "count" to total))
            albumList.addAll(albumHashMap.values)
            return albumList
        }
        return listOf()
    }

    fun listImages(albumId: String, newest: Boolean, total: Int, skip: Int?, take: Int?): Map<String, Any> {
        val media = mutableListOf<Map<String, Any?>>()
        val offset = skip ?: 0
        val limit = take ?: (total - offset)

        this.context?.run {
            val imageCursor: Cursor?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imageCursor = this.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageMetadataProjection,
                    android.os.Bundle().apply {
                        // Limit & Offset
                        putInt(android.content.ContentResolver.QUERY_ARG_LIMIT, limit)
                        putInt(android.content.ContentResolver.QUERY_ARG_OFFSET, offset)
                        // Sort
                        putStringArray(
                            android.content.ContentResolver.QUERY_ARG_SORT_COLUMNS,
                            arrayOf(
                                MediaStore.Images.Media.DATE_TAKEN,
                                MediaStore.Images.Media.DATE_MODIFIED
                            )
                        )
                        putInt(
                            android.content.ContentResolver.QUERY_ARG_SORT_DIRECTION,
                            if (newest) {
                                android.content.ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                            } else {
                                android.content.ContentResolver.QUERY_SORT_DIRECTION_ASCENDING
                            }
                        )
                        // Selection
                        if (albumId != allAlbumId) {
                            putString(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION, "${MediaStore.Images.Media.BUCKET_ID} = ?")
                            putStringArray(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, arrayOf(albumId))
                        }
                    },
                    null
                )
            } else {
                imageCursor = this.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    imageMetadataProjection,
                    if (albumId == allAlbumId) null else "${MediaStore.Images.Media.BUCKET_ID} = $albumId",
                    null,
                    "$imageOrderBy LIMIT $limit OFFSET $offset"
                )
            }

            imageCursor?.use { cursor ->
                while (cursor.moveToNext()) {
                    media.add(getImageMetadata(cursor))
                }
            }
        }

        return mapOf(
            "newest" to newest,
            "start" to offset,
            "total" to total,
            "items" to media
        )
    }

    fun listVideos(albumId: String, newest: Boolean, total: Int, skip: Int?, take: Int?): Map<String, Any> {
        val media = mutableListOf<Map<String, Any?>>()
        val offset = skip ?: 0
        val limit = take ?: (total - offset)

        this.context?.run {
            val videoCursor: Cursor?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                videoCursor = this.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    videoMetadataProjection,
                    android.os.Bundle().apply {
                        // Limit & Offset
                        putInt(android.content.ContentResolver.QUERY_ARG_LIMIT, limit)
                        putInt(android.content.ContentResolver.QUERY_ARG_OFFSET, offset)
                        // Sort
                        putStringArray(
                            android.content.ContentResolver.QUERY_ARG_SORT_COLUMNS,
                            arrayOf(
                                MediaStore.Video.Media.DATE_TAKEN,
                                MediaStore.Video.Media.DATE_MODIFIED
                            )
                        )
                        putInt(
                            android.content.ContentResolver.QUERY_ARG_SORT_DIRECTION,
                            if (newest) {
                                android.content.ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                            } else {
                                android.content.ContentResolver.QUERY_SORT_DIRECTION_ASCENDING
                            }
                        )
                        // Selection
                        if (albumId != allAlbumId) {
                            putString(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION, "${MediaStore.Video.Media.BUCKET_ID} = ?")
                            putStringArray(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, arrayOf(albumId))
                        }
                    },
                    null
                )
            } else {
                videoCursor = this.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    videoMetadataProjection,
                    if (albumId == allAlbumId) null else "${MediaStore.Video.Media.BUCKET_ID} = $albumId",
                    null,
                    "$videoOrderBy LIMIT $limit OFFSET $offset"
                )
            }

            videoCursor?.use { cursor ->
                while (cursor.moveToNext()) {
                    media.add(getVideoMetadata(cursor))
                }
            }
        }

        return mapOf(
            "newest" to newest,
            "start" to offset,
            "total" to total,
            "items" to media
        )
    }

    fun getMedium(mediumId: String, mediumType: String?): Map<String, Any?>? {
        return when (mediumType) {
            imageType -> {
                getImageMedia(mediumId)
            }
            videoType -> {
                getVideoMedia(mediumId)
            }
            else -> {
                getImageMedia(mediumId) ?: getVideoMedia(mediumId)
            }
        }
    }

    fun getImageMedia(mediumId: String): Map<String, Any?>? {
        var imageMetadata: Map<String, Any?>? = null

        this.context?.run {
            val imageCursor = this.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageMetadataProjection,
                "${MediaStore.Images.Media._ID} = $mediumId",
                null,
                null
            )

            imageCursor?.use { cursor ->
                if (cursor.moveToFirst()) {
                    imageMetadata = getImageMetadata(cursor)
                }
            }
        }

        return imageMetadata
    }

    fun getVideoMedia(mediumId: String): Map<String, Any?>? {
        var videoMetadata: Map<String, Any?>? = null

        this.context?.run {
            val videoCursor = this.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                videoMetadataProjection,
                "${MediaStore.Images.Media._ID} = $mediumId",
                null,
                null)

            videoCursor?.use { cursor ->
                if (cursor.moveToFirst()) {
                    videoMetadata = getVideoMetadata(cursor)
                }
            }
        }

        return videoMetadata
    }

    fun getThumbnail(mediumId: String, mediumType: String?, width: Int?, height: Int?, highQuality: Boolean?): ByteArray? {
        return when (mediumType) {
            imageType -> {
                getImageThumbnail(mediumId, width, height, highQuality)
            }
            videoType -> {
                getVideoThumbnail(mediumId, width, height, highQuality)
            }
            else -> {
                getImageThumbnail(mediumId, width, height, highQuality)
                    ?: getVideoThumbnail(mediumId, width, height, highQuality)
            }
        }
    }

    fun getImageThumbnail(mediumId: String, width: Int?, height: Int?, highQuality: Boolean?): ByteArray? {
        var byteArray: ByteArray? = null

        val bitmap: Bitmap? = this.context?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    val widthSize = width ?: if (highQuality == true) 512 else 96
                    val heightSize = height ?: if (highQuality == true) 384 else 96
                    this.contentResolver.loadThumbnail(
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediumId.toLong()),
                        Size(widthSize, heightSize),
                        null
                    )
                } catch (e: Exception) {
                    null
                }
            } else {
                val kind = if (highQuality == true) MediaStore.Images.Thumbnails.MINI_KIND else MediaStore.Images.Thumbnails.MICRO_KIND

                val cursor = this.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(
                        MediaStore.Images.Media.ORIENTATION
                    ),
                    "${MediaStore.Images.Media._ID} = $mediumId",
                    null,
                    null)

                var degress = 0

                cursor?.use { cursor ->
                    if(cursor.moveToFirst()) {
                        val orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION)

                        if(orientationColumn != -1) {
                            degress = cursor.getInt(orientationColumn)
                        }
                    }
                }

                var bmp = MediaStore.Images.Thumbnails.getThumbnail(
                    this.contentResolver, mediumId.toLong(),
                    kind, null
                )

                if(bmp != null && degress != 0) {
                    val mat = Matrix()
                    mat.setRotate(degress.toFloat(), bmp.width.toFloat() / 2, bmp.height.toFloat() / 2);
                    bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, mat, true)
                }

                bmp
            }
        }
        bitmap?.run {
            ByteArrayOutputStream().use { stream ->
                this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                byteArray = stream.toByteArray()
            }
        }

        return byteArray
    }

    fun getVideoThumbnail(mediumId: String, width: Int?, height: Int?, highQuality: Boolean?): ByteArray? {
        var byteArray: ByteArray? = null

        val bitmap: Bitmap? = this.context?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                try {
                    val widthSize = width ?: if (highQuality == true) 512 else 96
                    val heightSize = height ?: if (highQuality == true) 384 else 96
                    this.contentResolver.loadThumbnail(
                        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, mediumId.toLong()),
                        Size(widthSize, heightSize),
                        null
                    )
                } catch (e: Exception) {
                    null
                }
            } else {
                val kind = if (highQuality == true) MediaStore.Images.Thumbnails.MINI_KIND else MediaStore.Images.Thumbnails.MICRO_KIND
                MediaStore.Video.Thumbnails.getThumbnail(
                    this.contentResolver, mediumId.toLong(),
                    kind,null
                )
            }
        }
        bitmap?.run {
            ByteArrayOutputStream().use { stream ->
                this.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                byteArray = stream.toByteArray()
            }
        }

        return byteArray
    }

    fun getAlbumThumbnail(albumId: String, mediumType: String?, width: Int?, height: Int?, highQuality: Boolean?): ByteArray? {
        return when (mediumType) {
            imageType -> {
                getImageAlbumThumbnail(albumId, width, height, highQuality)
            }
            videoType -> {
                getVideoAlbumThumbnail(albumId, width, height, highQuality)
            }
            else -> {
                getImageAlbumThumbnail(albumId, width, height, highQuality)
                    ?: getVideoAlbumThumbnail(albumId, width, height, highQuality)
            }
        }
    }

    fun getImageAlbumThumbnail(albumId: String, width: Int?, height: Int?, highQuality: Boolean?): ByteArray? {
        return this.context?.run {
            val imageCursor: Cursor?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imageCursor = this.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Images.Media._ID),
                    android.os.Bundle().apply {
                        // Limit
                        putInt(android.content.ContentResolver.QUERY_ARG_LIMIT, 1)
                        // Sort
                        putStringArray(
                            android.content.ContentResolver.QUERY_ARG_SORT_COLUMNS,
                            arrayOf(
                                MediaStore.Images.Media.DATE_TAKEN,
                                MediaStore.Images.Media.DATE_MODIFIED
                            )
                        )
                        putInt(
                            android.content.ContentResolver.QUERY_ARG_SORT_DIRECTION,
                            android.content.ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                        )
                        // Selection
                        if (albumId != allAlbumId) {
                            putString(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION, "${MediaStore.Images.Media.BUCKET_ID} = ?")
                            putStringArray(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, arrayOf(albumId))
                        }
                    },
                    null
                )
            } else {
                imageCursor = this.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Images.Media._ID),
                    if (albumId == allAlbumId) null else "${MediaStore.Images.Media.BUCKET_ID} = $albumId",
                    null,
                    MediaStore.Images.Media.DATE_TAKEN + " DESC LIMIT 1"
                )
            }

            imageCursor?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(idColumn)
                    return@run getImageThumbnail(id.toString(), width, height, highQuality)
                }
            }

            return null
        }
    }

    fun getVideoAlbumThumbnail(albumId: String, width: Int?, height: Int?, highQuality: Boolean?): ByteArray? {
        return this.context?.run {
            val videoCursor: Cursor?

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                videoCursor = this.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Video.Media._ID),
                    android.os.Bundle().apply {
                        // Limit
                        putInt(android.content.ContentResolver.QUERY_ARG_LIMIT, 1)
                        // Sort
                        putStringArray(
                            android.content.ContentResolver.QUERY_ARG_SORT_COLUMNS,
                            arrayOf(
                                MediaStore.Video.Media.DATE_TAKEN,
                                MediaStore.Video.Media.DATE_MODIFIED
                            )
                        )
                        putInt(
                            android.content.ContentResolver.QUERY_ARG_SORT_DIRECTION,
                            android.content.ContentResolver.QUERY_SORT_DIRECTION_DESCENDING
                        )
                        // Selection
                        if (albumId != allAlbumId) {
                            putString(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION, "${MediaStore.Video.Media.BUCKET_ID} = ?")
                            putStringArray(android.content.ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, arrayOf(albumId))
                        }
                    },
                    null
                )
            } else {
                videoCursor = this.contentResolver.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    arrayOf(MediaStore.Video.Media._ID),
                    if (albumId == allAlbumId) null else "${MediaStore.Video.Media.BUCKET_ID} = $albumId",
                    null,
                    MediaStore.Video.Media.DATE_TAKEN + " DESC LIMIT 1"
                )
            }

            videoCursor?.use { cursor ->
                if (cursor.moveToNext()) {
                    val idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID)
                    val id = cursor.getLong(idColumn)
                    return@run getVideoThumbnail(id.toString(), width, height, highQuality)
                }
            }

            return null
        }
    }

    fun getFile(mediumId: String, mediumType: String?, mimeType: String?): String? {
        return when (mediumType) {
            imageType -> {
                getImageFile(mediumId, mimeType = mimeType)
            }
            videoType -> {
                getVideoFile(mediumId)
            }
            else -> {
                getImageFile(mediumId, mimeType = mimeType) ?: getVideoFile(mediumId)
            }
        }
    }

    fun getImageFile(mediumId: String, mimeType: String? = null): String? {
        this.context?.run {
            mimeType?.let {
                val type = this.contentResolver.getType(
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediumId.toLong())
                )
                if (it != type) {
                    return cacheImage(mediumId, it)
                }
            }

            val imageCursor = this.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media.DATA),
                "${MediaStore.Images.Media._ID} = $mediumId",
                null,
                null
            )

            imageCursor?.use { cursor ->
                if (cursor.moveToNext()) {
                    val dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                    return cursor.getString(dataColumn)
                }
            }
        }

        return null
    }

    fun getVideoFile(mediumId: String): String? {
        var path: String? = null

        this.context?.run {
            val videoCursor = this.contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Images.Media.DATA),
                "${MediaStore.Images.Media._ID} = $mediumId",
                null,
                null)

            videoCursor?.use { cursor ->
                if (cursor.moveToNext()) {
                    val dataColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
                    path = cursor.getString(dataColumn)
                }
            }
        }

        return path
    }

    fun cacheImage(mediumId: String, mimeType: String): String? {
        val bitmap: Bitmap? = this.context?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                try {
                    ImageDecoder.decodeBitmap(ImageDecoder.createSource(
                        this.contentResolver,
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediumId.toLong())
                    ))
                } catch (e: Exception) {
                    null
                }
            } else {
                MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mediumId.toLong())
                )
            }
        }

        bitmap?.let {
            val compressFormat: Bitmap.CompressFormat
            if (mimeType == "image/jpeg") {
                val path = File(getCachePath(), "$mediumId.jpeg")
                val out = FileOutputStream(path)
                compressFormat = Bitmap.CompressFormat.JPEG
                it.compress(compressFormat, 100, out)
                return path.absolutePath
            } else if (mimeType == "image/png") {
                val path = File(getCachePath(), "$mediumId.png")
                val out = FileOutputStream(path)
                compressFormat = Bitmap.CompressFormat.PNG
                it.compress(compressFormat, 100, out)
                return path.absolutePath
            } else if (mimeType == "image/webp") {
                val path = File(getCachePath(), "$mediumId.webp")
                val out = FileOutputStream(path)
                compressFormat = Bitmap.CompressFormat.WEBP
                it.compress(compressFormat, 100, out)
                return path.absolutePath
            }
        }

        return null
    }

    fun getImageMetadata(cursor: Cursor): Map<String, Any?> {
        val idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID)
        val widthColumn = cursor.getColumnIndex(MediaStore.Images.Media.WIDTH)
        val heightColumn = cursor.getColumnIndex(MediaStore.Images.Media.HEIGHT)
        val mimeColumn = cursor.getColumnIndex(MediaStore.Images.Media.MIME_TYPE)
        val dateTakenColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN)
        val dateModifiedColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)

        val id = cursor.getLong(idColumn)
        val width = cursor.getLong(widthColumn)
        val height = cursor.getLong(heightColumn)
        val mimeType = cursor.getString(mimeColumn)
        var dateTaken: Long? = null
        if (cursor.getType(dateTakenColumn) == FIELD_TYPE_INTEGER) {
            dateTaken = cursor.getLong(dateTakenColumn)
        }
        var dateModified: Long? = null
        if (cursor.getType(dateModifiedColumn) == FIELD_TYPE_INTEGER) {
            dateModified = cursor.getLong(dateModifiedColumn) * 1000
        }

        return mapOf(
            "id" to id.toString(),
            "mediumType" to imageType,
            "width" to width,
            "height" to height,
            "mimeType" to mimeType,
            "creationDate" to dateTaken,
            "modifiedDate" to dateModified
        )
    }

    fun getVideoMetadata(cursor: Cursor): Map<String, Any?> {
        val idColumn = cursor.getColumnIndex(MediaStore.Video.Media._ID)
        val widthColumn = cursor.getColumnIndex(MediaStore.Video.Media.WIDTH)
        val heightColumn = cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT)
        val mimeColumn = cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE)
        val durationColumn = cursor.getColumnIndex(MediaStore.Video.Media.DURATION)
        val dateTakenColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN)
        val dateModifiedColumn = cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED)

        val id = cursor.getLong(idColumn)
        val width = cursor.getLong(widthColumn)
        val height = cursor.getLong(heightColumn)
        val mimeType = cursor.getString(mimeColumn)
        val duration = cursor.getLong(durationColumn)
        var dateTaken: Long? = null
        if (cursor.getType(dateTakenColumn) == FIELD_TYPE_INTEGER) {
            dateTaken = cursor.getLong(dateTakenColumn)
        }
        var dateModified: Long? = null
        if (cursor.getType(dateModifiedColumn) == FIELD_TYPE_INTEGER) {
            dateModified = cursor.getLong(dateModifiedColumn) * 1000
        }

        return mapOf(
            "id" to id.toString(),
            "mediumType" to videoType,
            "width" to width,
            "height" to height,
            "mimeType" to mimeType,
            "duration" to duration,
            "creationDate" to dateTaken,
            "modifiedDate" to dateModified
        )
    }

    fun getCachePath(): File? {
        return this.context?.run {
            val cachePath = File(this.cacheDir, "photo_gallery")
            if (!cachePath.exists()) {
                cachePath.mkdirs()
            }
            return@run cachePath
        }
    }

    fun cleanCache() {
        val cachePath = getCachePath()
        cachePath?.deleteRecursively()
    }

    fun checkPermission() {

    }
}

class BackgroundAsyncTask<T>(val handler: () -> T, val post: (result: T) -> Unit) : AsyncTask<Void, Void, T>() {
    init {
        execute()
    }

    override fun doInBackground(vararg params: Void?): T {
        return handler()
    }

    override fun onPostExecute(result: T) {
        super.onPostExecute(result)
        post(result)
        return
    }
}
