package com.olshevchenko.currencyconverter.core.usecase

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

typealias FuncHandler = () -> Unit

abstract class RXUseCase<T, in Params>(
    private val subscribeScheduler: Scheduler,
    private val observeScheduler: Scheduler
) {

    private val disposables = CompositeDisposable()

    abstract fun buildUseCaseSingle(params: Params?): Single<T>

    fun execute(
        observer: SingleObserver<T>,
        onSubscribeHandler: FuncHandler, // lambda from presentation/UI to start loading show
        finallyHandler: FuncHandler, // lambda to finish loading show
        params: Params? = null
    ) {
        val observable: Single<T> = this.buildUseCaseSingle(params)
            .subscribeOn(subscribeScheduler)
            .observeOn(observeScheduler)
            .doOnSubscribe { onSubscribeHandler() }
            .doFinally { finallyHandler() }

        (observable.subscribeWith(observer) as? Disposable)?.let {
            disposables.add(it)
        }
    }

    fun dispose() {
        disposables.clear()
    }
}
