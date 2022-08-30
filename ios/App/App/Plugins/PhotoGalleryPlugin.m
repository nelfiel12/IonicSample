//
//  PhotoGalleryPlugin.m
//  App
//
//  Created by 모비핀 on 2022/08/30.
//

#import <Capacitor/Capacitor.h>
CAP_PLUGIN(PhotoGalleryPlugin, "PhotoGallery",
           CAP_PLUGIN_METHOD(listAlbums, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(listMedia, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getMedium, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getThumbnail, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getAlbumThumbnail, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getFile, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(test, CAPPluginReturnPromise);
)



//@objc public func listAlbums(_ call: CAPPluginCall) {
//    call.resolve()
//}
//@objc public func listMedia(_ call: CAPPluginCall) {
//    call.resolve()
//}
//@objc public func getMedium(_ call: CAPPluginCall) {
//    call.resolve()
//}
//@objc public func getThumbnail(_ call: CAPPluginCall) {
//    call.resolve()
//}
//@objc public func getAlbumThumbnail(_ call: CAPPluginCall) {
//    call.resolve()
//}
//@objc public func getFile(_ call: CAPPluginCall) {
//    call.resolve()
//}
//@objc public func test(_ call: CAPPluginCall) {
//    call.resolve()
//}
