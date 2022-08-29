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
        
        <ion-content :fullscreen="true" style="--background: transparent">
            <ion-button @click="takePicture">takePicture</ion-button>
            <ion-button @click="start">st</ion-button>
            <ion-button @click="stop">e</ion-button>
            <div style="width: 100%; height: 100%; --background: transparent">
                aa
            </div>
        </ion-content>
    </ion-page>
</template>

<script>
import { IonButtons, IonButton, IonContent, IonHeader, IonBackButton, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { CameraPreview } from '@capacitor-community/camera-preview'

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
        // CameraPreview.start()
        this.start()
        return
    },
    unmounted() {
        // CameraPreview.stop()
        this.stop()
    },
    methods: {
        async takePicture() {
            let ret = await CameraPreview.capture()

            const base64 = ret.value

            console.log('data size ' + base64.length)

            this.$router.push({
                    path : '/image',
                    query : {
                        base64 : base64
                    }
                });

            console.log('/image')

            return
        },
        start() {
            CameraPreview.start({
                x: 0,
                y: 0,
                toBack: true
            })
        },
        stop() {
            CameraPreview.stop()
        }
    }
  
        
}
</script>

<style scoped>
</style>