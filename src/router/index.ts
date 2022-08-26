import { createRouter, createWebHistory } from '@ionic/vue-router';
import { RouteRecordRaw } from 'vue-router';

const routes: Array<RouteRecordRaw> = [
  {
    path: '',
    redirect: '/folder/Inbox'
  },
  {
    path: '/folder/:id',
    component: () => import ('../views/FolderPage.vue')
  },
  {
    path: '/album',
    component: () => import('../views/AlbumPage.vue'),
  },
  {
    path: '/images',
    component: () => import('../views/ImageListPage.vue'),
  },  
  {
    path:'/photolib',
    component: () => import('../views/PhotoLibrary.vue'),
  },
  
  {
    path:'/image',
    component: () => import('../views/ImagePage.vue'),
  },
  {
    path:'/lazyload',
    component: () => import('../views/LazyLoadPage.vue'),
  },
  {
    path:'/canvas',
    component: () => import('../views/ImagePage.vue'),
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
