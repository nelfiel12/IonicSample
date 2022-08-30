import { registerPlugin } from '@capacitor/core';
import { PhotoGalleryPlugin } from './PhotoGalleryPlugin';
import { SocialLoginPlugin } from './SocialLogin';


const PhotoGallery = registerPlugin<PhotoGalleryPlugin>(
    'PhotoGallery',    
    {
        web: () => import('./web').then(m => new m.PhotoGalleryWeb())
    }
);


const SocialLogin = registerPlugin<SocialLoginPlugin>(
    'SocialLogin'

)

export { PhotoGallery, SocialLogin };