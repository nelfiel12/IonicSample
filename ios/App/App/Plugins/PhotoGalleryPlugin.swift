//
//  PhotoGalleryPlugin.swift
//  App
//
//  Created by 모비핀 on 2022/08/30.
//

import Foundation
import Capacitor

@objc(PhotoGalleryPlugin)
public class PhotoGalleryPlugin : CAPPlugin {

    @objc public func listAlbums(_ call: CAPPluginCall) {
        print(#function)
        call.resolve(["data" : ""])
    }
    @objc public func listMedia(_ call: CAPPluginCall) {
        print(#function)
        call.resolve()
    }
    @objc public func getMedium(_ call: CAPPluginCall) {
        print(#function)
        call.resolve()
    }
    @objc public func getThumbnail(_ call: CAPPluginCall) {
        print(#function)
        call.resolve()
    }
    @objc public func getAlbumThumbnail(_ call: CAPPluginCall) {
        print(#function)
        call.resolve()
    }
    @objc public func getFile(_ call: CAPPluginCall) {
        print(#function)
        call.resolve()
    }
    @objc public func test(_ call: CAPPluginCall) {
        print(#function)
        call.resolve()
    }
}
