<template>
    <ion-page>
        <ion-header :translucent="true">
        <ion-toolbar>
            <ion-buttons slot="start">
                <ion-back-button default-href="/"></ion-back-button>
            </ion-buttons>
            <ion-title>Album</ion-title>
        </ion-toolbar>
        </ion-header>
        
        <ion-content :fullscreen="true">
            <ion-button @click="test">test</ion-button>
            <ion-button @click="talk">talk</ion-button>
            <ion-button @click="account">account</ion-button>
        </ion-content>
    </ion-page>
</template>

<script>
import { IonButtons, IonButton, IonContent, IonHeader, IonBackButton, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { SocialLogin } from '@/plugins/index'

export default {
    components: {
        IonButton,
        IonButtons,
        IonContent,
        IonHeader,
        IonPage,
        IonTitle,
        IonToolbar,
        IonBackButton
    },
    data() {
        return {
        };
    },
    mounted() {
        return
    },
    methods: {
        test() {
            console.log('test')
            SocialLogin.isKakaoTalkLoginAvailable()
        },
        talk() {
            SocialLogin.loginWithKakaoTalk()
        },
        account() {
            SocialLogin.loginWithKakaoAccount()
        }
    }
  
        
}
</script>

<style scoped>
</style>