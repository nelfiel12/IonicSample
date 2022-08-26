package com.pscheol.ionicsample.plugins

import android.Manifest
import android.content.Intent
import android.util.Log
import com.getcapacitor.*
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.getcapacitor.annotation.PermissionCallback
import com.pscheol.ionicsample.TestActivity
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.*





@CapacitorPlugin(
    name = "PhotoGallery",
    permissions = [
        Permission(strings = [Manifest.permission.READ_EXTERNAL_STORAGE], alias = PhotoGalleryPlugin.READ_EXTERNAL_STORATE_ALIAS)
    ]
)
class PhotoGalleryPlugin : Plugin() {

    companion object {
        const val READ_EXTERNAL_STORATE_ALIAS = "read_alias"
        const val TAG = "PhotoGallery"
    }

    class TestRun {

        val call: PluginCall
        val run: () -> JSObject
        constructor(call: PluginCall, runnable: () -> JSObject) {
            this.call = call
            this.run = runnable
        }

        fun execute() {
            Log.d(TAG, "execute")
            CoroutineScope(Dispatchers.IO).launch {
                Log.d(TAG, "launch")
                val ret = run()

                call.resolve(ret)
                Log.d(TAG, "launch end")
            }
        }
    }

    lateinit var gallery: PhotoGallery

    var permissionResultRun: TestRun? = null


    override fun load() {
        super.load()
        gallery = PhotoGallery(activity)

    }

    fun checkPermission(call: PluginCall, runnable: () -> JSObject) {

        val execute = TestRun(call, runnable)

        if(PermissionState.GRANTED.equals(getPermissionState(READ_EXTERNAL_STORATE_ALIAS))) {

            execute.execute()
        } else {
            permissionResultRun =execute
            requestPermissionForAlias(READ_EXTERNAL_STORATE_ALIAS, call, "handleReadExternalPermissionResult")
        }
    }

    @PluginMethod
    fun listAlbums(call: PluginCall) {
        PluginMethod.RETURN_CALLBACK
        checkPermission(call) {
            val mediumType: String? = call.getString("mediumType")

            var ret = JSObject()

            if (mediumType != null) {
                var list = gallery.listAlbums(mediumType)

                val array = JSONArray()
                for (data in list) {

                    var temp = JSObject()
                    for(entry in data.entries) {
                        temp.put(entry.key, entry.value)
                    }

                    var obj = JSONObject(data)

                    array.put(obj)
                }

                ret.put("data", array.toString())
            }

            ret
        }
    }

    @PermissionCallback
    private fun handleReadExternalPermissionResult(call: PluginCall) {
        if(PermissionState.GRANTED.equals(getPermissionState(READ_EXTERNAL_STORATE_ALIAS))) {
            permissionResultRun?.execute()
            permissionResultRun = null
        } else {
            call.reject("Permission failed")
        }
    }


    @PluginMethod
    fun listMedia(call: PluginCall) {
        checkPermission(call) {
            val albumId = call.getString("albumId")
            val mediumType = call.getString("mediumType")
            val newest = call.getBoolean("newest")
            val total = call.getInt("total")
            val skip = call.getInt("skip")
            val take = call.getInt("take")

            val ret = JSObject()

            if(albumId != null && newest != null && total != null) {
                val map = when (mediumType) {
                    PhotoGallery.imageType -> gallery.listImages(albumId, newest, total, skip, take)
                    PhotoGallery.videoType -> gallery.listVideos(albumId, newest, total, skip, take)
                    else -> null
                }

                if(map != null) {
                    ret.put("data", JSONObject(map).toString())
                }
            }
            ret
        }
    }

    @PluginMethod
    fun getMedium(call: PluginCall) {
        checkPermission(call) {
            val mediumId = call.getString("mediumId")
            val mediumType = call.getString("mediumType")

            val ret = JSObject()
            if(mediumId != null) {
                val map = gallery.getMedium(mediumId, mediumType)

                if(map != null) {
                    ret.put("data", JSONObject(map).toString())
                }
            }
            ret
        }
    }

    @PluginMethod
    fun getThumbnail(call: PluginCall) {
        checkPermission(call) {
            val mediumId = call.getString("mediumId")
            val mediumType = call.getString("mediumType")
            val width = call.getInt("width")
            val height = call.getInt("height")
            val highQuality = call.getBoolean("highQuality")

            val ret = JSObject()

            if(mediumId != null) {
                val byteArray = gallery.getThumbnail(mediumId, mediumType, width, height, highQuality)

                if(byteArray != null) {
                    val value = Base64.getEncoder().encodeToString(byteArray)
                    ret.put("data", value)
                }
            }

            ret
        }
    }

    @PluginMethod
    fun getAlbumThumbnail(call: PluginCall) {
        checkPermission(call) {
            val albumId = call.getString("albumId")
            val mediumType = call.getString("mediumType")
            val width = call.getInt("width")
            val height = call.getInt("height")
            val highQuality = call.getBoolean("highQuality")

            val ret = JSObject()
            if(albumId != null) {
                val byteArray = gallery.getAlbumThumbnail(albumId, mediumType, width, height, highQuality)

                if(byteArray != null) {
                    val value = Base64.getEncoder().encodeToString(byteArray)

                    ret.put("data", value)
                }
            }

            ret
        }
    }

    @PluginMethod
    fun getFile(call: PluginCall) {
        checkPermission(call) {
            val mediumId = call.getString("mediumId")
            val mediumType = call.getString("mediumType")
            val mimeType = call.getString("mimeType")

            val ret = JSObject()

            if(mediumId != null) {
                var file = gallery.getFile(mediumId, mediumType, mimeType)
                ret.put("data", file)

                val f = File(file)
                val byte = f.readBytes()

                val temp = Base64.getEncoder().encodeToString(byte)

                ret.put("byte", temp)

            }

            ret
        }
    }

    @PluginMethod
    fun cleanCache(call: PluginCall) {

        gallery.cleanCache()
        call.resolve()
    }

    private fun callResolve(call: PluginCall) {

    }

    @PluginMethod
    fun test(call: PluginCall) {
        activity.startActivity(Intent(activity.applicationContext, TestActivity::class.java))
        call.resolve()
    }
}