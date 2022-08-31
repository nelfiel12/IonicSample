//
//  RxCreator.swift
//  App
//
//  Created by 모비핀 on 2022/08/31.
//

import Foundation
import RxSwift

public class RxCreator<T> {
    static func create2(_ callback: @escaping (AnyObserver<T>) -> Void) ->Observable<T> {
        return Observable<T>.create { observer in
            callback(observer)
            return Disposables.create()
        }
    }
}
