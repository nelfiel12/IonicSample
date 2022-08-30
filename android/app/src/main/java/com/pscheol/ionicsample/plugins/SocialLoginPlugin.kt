package com.pscheol.ionicsample.plugins

import android.Manifest
import com.getcapacitor.JSObject
import com.getcapacitor.Plugin
import com.getcapacitor.PluginCall
import com.getcapacitor.PluginMethod
import com.getcapacitor.PluginResult
import com.getcapacitor.annotation.CapacitorPlugin
import com.getcapacitor.annotation.Permission
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import java.util.*


@CapacitorPlugin(
    name = "SocialLogin"
)
class SocialLoginPlugin : Plugin() {

    @PluginMethod
    fun isKakaoTalkLoginAvailable(call: PluginCall) {
        val ret = UserApiClient.instance.isKakaoTalkLoginAvailable(activity)
        val obj = JSObject()
        obj.put("available", ret)
        call.resolve(obj)
    }

    private fun loginResult(call: PluginCall, token: OAuthToken?, error: Throwable?) {
        if(error != null) {
            call.reject(error.message)
        }
        else if(token == null) {
            call.reject("")
        }
        else {
            val obj = JSObject()
            obj.put("accessToken", token.accessToken)
            obj.put("accessTokenExpiresAt", token.accessTokenExpiresAt.toString())
            obj.put("refreshToken", token.refreshToken)
            obj.put("refreshTokenExpiresAt", token.refreshTokenExpiresAt.toString())
            obj.put("idToken", token.idToken)
            obj.put("scopes", token.accessToken)

            call.resolve(obj)
        }
    }

    @PluginMethod
    fun loginWithKakaoTalk(call: PluginCall) {
        UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
            loginResult(call, token, error)
        }
    }

    @PluginMethod
    fun loginWithKakaoAccount(call: PluginCall) {
        UserApiClient.instance.loginWithKakaoAccount(activity) { token, error ->
            loginResult(call, token, error)
        }
    }
}