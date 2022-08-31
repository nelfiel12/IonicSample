package com.pscheol.ionicsample.plugins

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter

class RxCreator {

    companion object {
        fun <T> create(callback: (ObservableEmitter<T>) -> Unit): Observable<T> {
            return Observable.create<T> {
                if (!it.isDisposed) {
                    callback(it)
                }
            }
        }
    }
}