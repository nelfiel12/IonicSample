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
    
    
    
    func isKakaoTalkLoginAvailable2() -> Observable<Bool> {
        
        
        return Observable<Bool>.create { observer in
            observer.onNext(UserApi.isKakaoTalkLoginAvailable())
            observer.onCompleted()
            
            
            return Disposables.create()
        }
    }
    
    
    func isKakaoTalkLoginAvailable() -> Observable<Bool> {
        return Observable<Bool>.create { observer in
            observer.onNext(UserApi.isKakaoTalkLoginAvailable())
            observer.onCompleted()
            
            
            return Disposables.create()
        }
    }
    
    
    private func loginWithKakao(observer: AnyObserver<KakaoSDKAuth.OAuthToken>, token: OAuthToken?, error: Error?) {
        if(error != nil) {
            observer.onError(error!)
        } else if(token == nil) {
            observer.onError(NSError(domain: "empty", code: 0))
        } else {
            observer.onNext(token!)
            observer.onCompleted()
        }
    }
    
    public func loginWithKakaoTalk() -> Observable<OAuthToken>{
        return Observable<KakaoSDKAuth.OAuthToken>.create { observer in
            UserApi.shared.loginWithKakaoTalk {(oauthToken, error) in
                
                self.loginWithKakao(observer: observer, token: oauthToken, error: error)
                
            }
            
            return Disposables.create()
        }
    }
    
    public func loginWithKakaoAccount() -> Observable<OAuthToken>{
        return Observable<KakaoSDKAuth.OAuthToken>.create { observer in
            UserApi.shared.loginWithKakaoAccount {(oauthToken, error) in
                
                self.loginWithKakao(observer: observer, token: oauthToken, error: error)
                
            }
            
            return Disposables.create()
        }
    }
    
    public func me() {
        
    }
}
