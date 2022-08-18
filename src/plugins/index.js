import { registerPlugin } from '@capacitor/core';

const PhotoGallery = registerPlugin(
    'PhotoGallery',    
    {
        web: () => import('./web').then(m => new m.PhotoGalleryWeb())
    }
);

export { PhotoGallery };