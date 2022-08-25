export type MediumType = "image" | "video";

export interface PhotoGalleryPlugin {
    listAlbums(opts: { mediumType: MediumType }) : Promise<{ data : string}>;

    listMedia(opts: {
        albumId : string,
        mediumType : MediumType,
        newest : boolean,
        total : number,
        skip : number,
        take : number,
    }) : Promise<{ data : string}>;

    getMedium(opts: {
        mediumId : string,
        mediumType : MediumType
    }) : Promise<{ data : string}>; 

    getAlbumThumbnail(opts:{
        albumId : string,
        mediumType : MediumType,
        width : number,
        height : number,
        highQuality : boolean
    }) : Promise<{ data : string}>;

    getFile(opts: {
        mediumId : string,
        mediumType : string,
        mimeType : string
    }) : Promise<{ data : string}>;
    
    cleanCache() : Promise<void>;

    test() : Promise<void>;
}