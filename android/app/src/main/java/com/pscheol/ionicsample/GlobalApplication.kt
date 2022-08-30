package com.pscheol.ionicsample

import android.app.Application
import android.os.Debug
import android.util.Log
import androidx.viewbinding.BuildConfig
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility

class GlobalApplication : Application() {

    companion object {
        const val TAG = "GlobalApplication"
    }

    override fun onCreate() {
        super.onCreate()
        // 다른 초기화 코드들

        // Kakao SDK 초기화
        KakaoSdk.init(this, "8154911ad2f2135cde32ff4addfd814a")

        if(!BuildConfig.DEBUG) {
            var keyHash = Utility.getKeyHash(this)

            Log.d(TAG, "Kakao keyHash : " + keyHash)
        }


    }
}
