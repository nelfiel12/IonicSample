package com.pscheol.ionicsample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.pscheol.ionicsample.databinding.ActivityTestBinding

class TestActivity : AppCompatActivity() {

    companion object {
        const val TAG = "TestActivity"
    }

    private lateinit var binding: ActivityTestBinding

    val requestActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            activityResult ->

        val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)

        try {
            val account = task.getResult(ApiException::class.java)


            return@registerForActivityResult
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode);

        }
    }

    private val callabck = object : OAuthLoginCallback {
        override fun onError(errorCode: Int, message: String) {
            onFailure(errorCode, message)
        }

        override fun onFailure(httpStatus: Int, message: String) {
            val errorCode = NaverIdLoginSDK.getLastErrorCode().code
            val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
            Toast.makeText(applicationContext,"errorCode:$errorCode, errorDesc:$errorDescription",Toast.LENGTH_SHORT).show()
        }

        override fun onSuccess() {
            Toast.makeText(applicationContext, "success", Toast.LENGTH_LONG).show()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_test)
        NaverIdLoginSDK.initialize(applicationContext, "EYi6puy_1a1kp1RHau3J", "kI0vilWKCF", "api test")


        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonKakao.setOnClickListener() {
            startActivity(Intent(this, KakaotestActivity::class.java))
        }

        binding.tvAccessToken.text = NaverIdLoginSDK.getAccessToken()
        binding.tvRefreshToken.text = NaverIdLoginSDK.getRefreshToken()
        binding.tvExpires.text = NaverIdLoginSDK.getExpiresAt().toString()
        binding.tvType.text = NaverIdLoginSDK.getTokenType()
        binding.tvState.text = NaverIdLoginSDK.getState().toString()



        binding.buttonOAuthLoginImg.setOAuthLoginCallback(callabck)

        binding.buttonLogin.setOnClickListener {
            Log.d(TAG, "click")

            NaverIdLoginSDK.authenticate(applicationContext, callabck)
/*
            val opt = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build()

            val client = GoogleSignIn.getClient(applicationContext, opt)






            val intent = client.signInIntent
            requestActivity.launch(intent)

 */
        }

    }

    override fun onDestroy() {
        super.onDestroy()
    }
}