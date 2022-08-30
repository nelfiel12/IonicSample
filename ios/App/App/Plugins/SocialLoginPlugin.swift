//
//  SocialLogin.swift
//  App
//
//  Created by 모비핀 on 2022/08/30.
//

import Capacitor
import KakaoSDKUser
import KakaoSDKAuth

@objc(SocialLoginPlugin)
public class SocialLoginPlugin : CAPPlugin {
    
    @objc public func isKakaoTalkLoginAvailable(_ call: CAPPluginCall) {
        let ret = KakaoSDKUser.UserApi.isKakaoTalkLoginAvailable()
        
        call.resolve(["available" : ret])
    }
    
    private func loginResult(_ call: CAPPluginCall, token: OAuthToken?, error: Error?) {
        
        
        if let error = error {
            print(error)
            call.reject("")
            
        }
        else if token == nil {
            call.reject("")
        }
        else {
            print("loginWithKakaoTalk() success.")

            let token: OAuthToken = token!
            
            let obj = [
                "accessToken" : token.accessToken,
                "accessTokenExpiresAt" : token.expiredAt.toString(),
                "refreshToken" : token.refreshToken,
                "refreshTokenExpiresAt" : token.refreshTokenExpiredAt.toString(),
//                "idToken", token.idToken,
                "scopes" : token.accessToken
            ]
            
            
            call.resolve(obj)
        }
    }
    
    
    @objc public func loginWithKakaoTalk(_ call: CAPPluginCall) {
        
        if(!UserApi.isKakaoTalkLoginAvailable()) {
            call.reject("")
            return
        }
        
                
        UserApi.shared.loginWithKakaoTalk {(oauthToken, error) in
            
            self.loginResult(call, token: oauthToken, error: error)
        }
    }
    
    @objc public func loginWithKakaoAccount(_ call: CAPPluginCall) {
        UserApi.shared.loginWithKakaoAccount {(oauthToken, error) in
            
            self.loginResult(call, token: oauthToken, error: error)
        }
    }
}


