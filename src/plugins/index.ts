import { registerPlugin } from '@capacitor/core';
import { PhotoGalleryPlugin } from './PhotoGalleryPlugin';

const PhotoGallery = registerPlugin<PhotoGalleryPlugin>(
    'PhotoGallery',    
    {
        web: () => import('./web').then(m => new m.PhotoGalleryWeb())
    }
);

export { PhotoGallery };