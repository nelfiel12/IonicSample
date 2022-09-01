//
//  RxCreator.swift
//  App
//
//  Created by 모비핀 on 2022/08/31.
//

import Foundation
import RxSwift

public class RxCreator<T> {
    static func create(_ callback: @escaping (@escaping Single<T>.SingleObserver) -> Void) ->Single<T> {
        return Single<T>.create { observer in
            callback(observer)

            return Disposables.create()
        }
    }
}
