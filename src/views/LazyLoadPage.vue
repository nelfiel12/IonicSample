<template>
    <ion-page>
        <ion-header :translucent="true">
        <ion-toolbar>
            <ion-buttons slot="start">
                <ion-back-button default-href="/"></ion-back-button>
            </ion-buttons>
            <ion-title>LazyLoad</ion-title>
        </ion-toolbar>
        </ion-header>
        <ion-content :fullscreen="true">
            <div class="layout_grid">
                <div v-for="i in 10000" :key="i" class="album_item" >
                    <img v-lazyload :data-src="'https://picsum.photos/seed/' + i + '/300/300'" />
                </div>
            </div>
        </ion-content>
    </ion-page>
</template>

<script>
import { IonButtons, IonButton, IonContent, IonHeader, IonBackButton, IonPage, IonTitle, IonToolbar } from '@ionic/vue';

const io = new IntersectionObserver((entries, observer) => {
    entries.forEach(entry => {
        if(entry.isIntersecting) {
            entry.target.src = entry.target.dataset.src
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
        };
    },
    mounted() {
        return;
    },

    methods: {
        
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
    width: 120px;
    height: 120px;
}
</style>