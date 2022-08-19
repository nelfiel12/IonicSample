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
        <ion-content>
            <img :src="src" />
        </ion-content>
        <ion-content>
            <!-- <img :src="'cdvphotolibrary://thumbnail?photoId=23393%3B%2Fstorage%2Femulated%2F0%2F%EB%B3%B4%EA%B3%A0%EC%9B%8D%EC%8A%A4%2Fa07cc0fc-9a94-84dc-daa6-b74429fe0091_1648022634239.png&width=512&height=384&quality=0.5'" /> -->
            <div class="layout_grid">
                <div v-for="item in list ?? []"  :key="item" class="album_item" @click="onClickItem(item)">
                    {{item}}
                </div>
            </div>
        </ion-content>
    </ion-page>
</template>

<script>
import { IonButtons, IonButton, IonContent, IonHeader, IonBackButton, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { PhotoLibrary } from '@ionic-native/photo-library'

export default {
    components: {
        IonButtons,
        IonContent,
        IonHeader,
        IonPage,
        IonTitle,
        IonToolbar,
        IonBackButton,
        //IonButton
    },
    data() {
        return {
            list : null,
            src : null
        };
    },
    mounted() {
        this.init();
    },
    methods: {
        async init() {
            await PhotoLibrary.requestAuthorization(
                {
                    read: true,
                    write: true
                }
            )

            PhotoLibrary.getLibrary().subscribe({
                next: library => {                    
                    this.list = library.library
                    
                    for(let item of this.list) {
                        let url;
                        if (item.id.split(';').length > 0) {
                            url = 'file://' + item.id.split(';')[1];
                        }
                        // Get http://localhost url
                        url = (window).Ionic.WebView.convertFileSrc(url)
                        item.url = url;
                        continue
                    }

                }
            })
        },
        async onClickItem(item) {
            
            this.src = item.url
        }
    }
  
        
}
</script>

<style scoped>
.layout_grid {
    display: grid;
    grid: '. . .';
}
   
.album_item {
    border: 1px solid black;
    width: 120px
}
</style>