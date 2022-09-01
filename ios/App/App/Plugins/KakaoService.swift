//
//  KakaoService.swift
//  App
//
//  Created by 모비핀 on 2022/08/31.
//

import Foundation
import RxSwift
import KakaoSDKUser
import KakaoSDKCommon
import KakaoSDKAuth

class KakaoService {
    
    static let errorObj = NSError(domain: "error", code: 0)
    
    func isKakaoTalkLoginAvailable() -> Single<Bool> {
        return RxCreator<Bool>.create { observer in
            observer(.success(UserApi.isKakaoTalkLoginAvailable()))
        }
    }
    
    
    private func loginWithKakao(observer: Single<OAuthToken>.SingleObserver, token: OAuthToken?, error: Error?) {
        if(error != nil || token == nil) {
            observer(.failure(error ?? KakaoService.errorObj))
        } else {
            observer(.success(token!))
            
        }
    }
    
    public func loginWithKakaoTalk() -> Single<OAuthToken>{
        return RxCreator<KakaoSDKAuth.OAuthToken>.create { observer in
            UserApi.shared.loginWithKakaoTalk {(oauthToken, error) in
                
                self.loginWithKakao(observer: observer, token: oauthToken, error: error)
                
            }
            
        }
    }
    
    public func loginWithKakaoAccount() -> Single<OAuthToken>{
        
        return RxCreator<KakaoSDKAuth.OAuthToken>.create { observer in
            UserApi.shared.loginWithKakaoAccount {(oauthToken, error) in
                
                self.loginWithKakao(observer: observer, token: oauthToken, error: error)
                
            }
            
        }
    }
    
    public func me() -> Single<User> {
        return RxCreator<User>.create { observer in
            UserApi.shared.me { user, error in
                
                if(error != nil || user == nil) {
                    observer(.failure(error ?? KakaoService.errorObj))
                } else {
                    observer(.success(user!))
                }
            }
        }
    }
}
