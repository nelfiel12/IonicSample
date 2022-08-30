//
//  SocialLoginPlugin.m
//  App
//
//  Created by 모비핀 on 2022/08/30.
//

#import <Capacitor/Capacitor.h>

CAP_PLUGIN(SocialLoginPlugin, "SocialLogin",
           CAP_PLUGIN_METHOD(isKakaoTalkLoginAvailable, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(loginWithKakaoTalk, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(loginWithKakaoAccount, CAPPluginReturnPromise);
)
