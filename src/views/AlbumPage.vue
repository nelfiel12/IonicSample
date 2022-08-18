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
            <div>                
                <ion-button v-for="item in list ?? []" :key="item" @click="onClickAlbum(item)">
                    {{item}}
                </ion-button>
            </div>
            
            <div class="layout_grid">
                <div v-for="item in album?.items ?? []"  :key="item" class="album_item" @click="onClickItem(item)">
                    {{item}}
                </div>
                
            </div>
        </ion-content>
    </ion-page>
</template>

<script>
import { IonButtons, IonButton, IonContent, IonHeader, IonBackButton, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { PhotoGallery } from '@/plugins/index'

export default {
    components: {
        IonButtons,
        IonButton,
        IonContent,
        IonHeader,
        IonPage,
        IonTitle,
        IonToolbar,
        IonBackButton
    },
    data() {
        return {
            list : null,            
            album : null
        };
    },
    mounted() {
        this.init();
    },
    methods: {
        async init() {
            const data = await PhotoGallery.listAlbums({ mediumType : "image"});

            if(data.data) {
                this.list = JSON.parse(data.data);
            }
        },
        async onClickAlbum(item) {
            const param = {
                albumId : item.id,
                mediumType : item.mediumType,
                newest : true,
                total : item.count
            };


            const ret = await PhotoGallery.listMedia(param);

            this.album = JSON.parse(ret.data);

            // listMedia(opts: {
            //     albumId : string,
            //     mediumType : MediumType,
            //     newest : boolean,
            //     total : number,
            //     skip : number,
            //     take : number,
            // }) : Promise<{ data : string}>;

            return;
        },
        async onClickItem(item) {
            this.$router.push({
                    path : '/image',
                    query : {
                        json : JSON.stringify(item)
                    }
                });
            console.log(JSON.stringify(item))
            return;
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
}
</style>