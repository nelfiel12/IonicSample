<template>
    <ion-page>
        <ion-header :translucent="true">
        <ion-toolbar>
            <ion-buttons slot="start">
                <ion-back-button default-href="/"></ion-back-button>
            </ion-buttons>
            <ion-title>Camera</ion-title>
        </ion-toolbar>
        </ion-header>
        
        <ion-content :fullscreen="true">
            <ion-button @click="takePicture">takePicture</ion-button>
        </ion-content>
    </ion-page>
</template>

<script>
import { IonButtons, IonButton, IonContent, IonHeader, IonBackButton, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { Camera, CameraResultType } from '@capacitor/camera'

export default {
    components: {
        IonButtons,
        IonContent,
        IonHeader,
        IonPage,
        IonTitle,
        IonToolbar,
        IonBackButton,
        IonButton
    },
    data() {
        return {
        };
    },
    mounted() {
        console.log('mounted')
        return
    },
    methods: {
        async takePicture() {
            const ret = await Camera.getPhoto({
                allowEditing: true,
                resultType: CameraResultType.Uri
            })

            return
        }
    }
  
        
}
</script>

<style scoped>
</style>