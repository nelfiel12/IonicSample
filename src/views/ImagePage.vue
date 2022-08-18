<template>
    <ion-page>
        <ion-header :translucent="true">
        <ion-toolbar>
            <ion-buttons slot="start">
                <ion-back-button default-href="/"></ion-back-button>
            </ion-buttons>
            <ion-title>Image</ion-title>
        </ion-toolbar>
        </ion-header>
        
        <ion-content :fullscreen="true">
            <div>
                <p>{{data}}</p>
                <img :src="src" />
            </div>
        </ion-content>
    </ion-page>
</template>

<script>
import { IonButtons, IonContent, IonHeader, IonBackButton, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { PhotoGallery } from '@/plugins/index'

export default {
    components: {
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
            image : null,
            data : null,
            src : null
        }
    },
    mounted() {
        const param = this.$route.query.json;

        if(param) {
            this.image = JSON.parse(param)

            this.init();
        }
    },
    methods: {
        async init() {
            const ret = await PhotoGallery.getFile({
                mediumId : this.image.id,
                mediumType : this.image.mediumType,
                mimeType : this.image.mimeType
            })

            this.data = ret.data;

            this.src = 'data:' + (this.image.mimeType ?? 'image') + ';base64,'+ret.byte;
        }
    }
}
</script>

<style>

</style>