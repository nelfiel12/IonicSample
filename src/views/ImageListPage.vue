<template>
    <ion-page>
        <ion-header :translucent="true">
        <ion-toolbar>
            <ion-buttons slot="start">
                <ion-back-button default-href="/"></ion-back-button>
            </ion-buttons>
            <ion-title>Images</ion-title>
        </ion-toolbar>
        </ion-header>
        
        <ion-content :fullscreen="true">
            <div>            
                {{album}}
            </div>
            
            <div class="layout_grid">
                <div v-for="item in albumData?.items ?? []"  :key="item" class="album_item" @click="onClickItem(item)">
                    <p>{{item.id}}</p>
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

            const ret = await PhotoGallery.getThumbnail({
                mediumId : id,
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
            album : null,
            albumData : null
        };
    },
    mounted() {

        const json = this.$route.query.json

        if(json) {
            this.album = JSON.parse(json)
        this.init();
        }

    },
    methods: {
        async init() {
            const item = this.album

            if(!item)
                return

            const param = {
                albumId : item.id,
                mediumType : item.mediumType,
                newest : true,
                total : item.count
            };


            const ret = await PhotoGallery.listMedia(param);

            this.albumData = JSON.parse(ret.data);
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
    grid-template-columns: repeat(3, 1fr);
    /* grid-template-rows: repeat(auto-fill, 33vw); */
    
}
   
.album_item {
    border: 1px solid black;
    aspect-ratio: 1;
}
</style>