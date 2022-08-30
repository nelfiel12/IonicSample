export interface SocialLoginPlugin {

    isKakaoTalkLoginAvailable() : Promise<{}>

    loginWithKakaoTalk() : Promise<{}>
    loginWithKakaoAccount() : Promise<{}>
}