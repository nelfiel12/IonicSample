package com.pscheol.ionicsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.pscheol.ionicsample.databinding.ActivityKakaotestBinding
import com.pscheol.ionicsample.databinding.ActivityTestBinding

class KakaotestActivity : AppCompatActivity() {

    companion object {
        const val TAG = "KakaoTest"
    }

    private lateinit var binding: ActivityKakaotestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_kakaotest)

        binding = ActivityKakaotestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textAvailable.text = "KakaoTalkLoginAvailable : " + UserApiClient.instance.isKakaoTalkLoginAvailable(applicationContext)


        binding.buttonTalk.setOnClickListener() {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")

                    binding.textAccesstoken.text = token.accessToken
                }
            }
        }


        binding.buttonAccount.setOnClickListener() {
            UserApiClient.instance.loginWithKakaoAccount(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "로그인 실패", error)
                }
                else if (token != null) {
                    Log.i(TAG, "로그인 성공 ${token.accessToken}")

                    binding.textAccesstoken.text = token.accessToken
                }
            }
        }
    }
}