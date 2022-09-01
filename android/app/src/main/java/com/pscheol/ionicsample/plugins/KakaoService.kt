package com.pscheol.ionicsample.plugins

import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.common.model.KakaoSdkError
import com.kakao.sdk.user.UserApi
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.AccessTokenInfo
import com.kakao.sdk.user.model.User
import com.kakao.sdk.user.rx
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.ThreadLocalRandom

class KakaoService {

    companion object {
        fun errorObj(): Throwable {
            return Throwable()
        }
    }

    fun accessTokenInfo() : Single<AccessTokenInfo> {
        return RxCreator.create {
            UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                if(error != null || tokenInfo == null)
                    it.onError(error ?: errorObj())
                else {
                    it.onSuccess(tokenInfo)
                }
            }
        }
    }

    fun hasToken() : Single<Boolean> {
        UserApiClient.rx.accessTokenInfo().subscribe().addTo(CompositeDisposable())
        return RxCreator.create {
            if (AuthApiClient.instance.hasToken()) {
                UserApiClient.instance.accessTokenInfo { _, error ->
                    var ret = false

                    if(error != null) {
                        if(error is KakaoSdkError && error.isInvalidTokenError()) {
                            ret = false
                        }
                        else {
                            it.onError(error)
                            return@accessTokenInfo
                        }

                    } else {
                        ret = true
                    }

                    it.onSuccess(ret)
                    
                }
            } else {
                it.onSuccess(false)
                
            }
        }
    }

    fun me() : Single<User> {
        return RxCreator.create<User> {
            UserApiClient.instance.me { user, error ->
                if(error != null || user == null) {
                    it.onError(error ?: errorObj())
                } else {
                    it.onSuccess(user)
                    
                }
            }
        }
    }
}