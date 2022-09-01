package com.pscheol.ionicsample.plugins

import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.core.SingleEmitter

class RxCreator {

    companion object {
        fun <T> create(callback: (SingleEmitter<T>) -> Unit): Single<T> {
            return Single.create {
                if (!it.isDisposed) {
                    callback(it)
                }
            }
        }
    }
}