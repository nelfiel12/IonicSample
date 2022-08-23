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
            <img :src="item?.url" @click="onClickImg"/>
        </ion-content>
        <ion-content>
            <div class="layout_grid">
                <div v-for="item in list"  :key="item" class="album_item" @click="onClickItem(item)">
                    <img v-lazyload :data-id="item.id" :data-type="item.mimeType" />
                    <p>{{item}}</p>
                </div>
            </div>
        </ion-content>
    </ion-page>
</template>

<script>
import { IonButtons, IonButton, IonContent, IonHeader, IonBackButton, IonPage, IonTitle, IonToolbar } from '@ionic/vue';
import { PhotoLibrary, PhotoLibraryOriginal } from '@ionic-native/photo-library'

const io = new IntersectionObserver((entries, observer) => {
    entries.forEach(async entry => {
        if(entry.isIntersecting) {
            const id = entry.target.dataset.id;

            const ret = await PhotoLibrary.getThumbnail(id)

            const buf = await ret.arrayBuffer()

            if(buf && buf.byteLength) {                
                entry.target.src = "data:image;base64," + btoa(String.fromCharCode.apply(null, new Uint8Array(buf)))
            }
            
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
        IonBackButton,
        //IonButton
    },
    data() {
        return {
            list : [],
            item : null
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
            this.item = item
        },
        onClickImg() {
            if(this.item) {
                this.$router.push({ 
                        path : '/image',
                        query : {
                            item : JSON.stringify(this.item)
                        }
                    })
            }
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