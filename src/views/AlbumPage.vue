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
            <div class="layout_grid">
                <div v-for="item in list"  :key="item" class="album_item" @click="onClickItem(item)">
                    <p>{{item.name}}</p>
                    <img v-lazyload :data-id="item.id" :data-type="item.mediumType" />
                </div>
                
            </div>
        </ion-content>
    </ion-page>
</template>

<script>
import { IonButtons, IonButton, IonContent, IonHeader, IonBackButton, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { PhotoGallery } from '@/plugins/index'

const io = new IntersectionObserver((entries, observer) => {
    entries.forEach(async entry => {
        console.log('aa')
        if(entry.isIntersecting) {
            const id = entry.target.dataset.id;
            const type = entry.target.dataset.type;

            // await PhotoGallery.getAlbumThumbnail()

            const ret = await PhotoGallery.getAlbumThumbnail({
                albumId : id,
                mediumType : type,
                width : 100,
                height : 100,
                highQuality : false
            })

            entry.target.src = "data:image;base64," + ret.data

            // const buf = await ret.arrayBuffer()

            // if(buf && buf.byteLength) {                
            //     entry.target.src = "data:image;base64," + btoa(String.fromCharCode.apply(null, new Uint8Array(buf)))
            // }
            
            observer.unobserve(entry.target)
        }
    })
})

export default {
    directives : {        
        lazyload : {
            mounted(el) {
                io.observe(el)
            },
            unmounted(el) {
                io.unobserve(el)
            }
        }
    },
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
            list : [],            
            album : {
                items : []
            }
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
        async onClickItem(item) {
            this.$router.push({
                    path : '/images',
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
    /* grid-template-columns: repeat(3, 33%); */
    /* grid-template-rows: repeat(auto-fill, 33vw); */
    
}
   
.album_item {
    border: 1px solid black;
    aspect-ratio: 1;
}
</style>