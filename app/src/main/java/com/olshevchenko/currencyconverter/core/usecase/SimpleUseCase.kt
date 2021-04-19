package com.olshevchenko.currencyconverter.core.usecase

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class SimpleUseCase<T, in Params> {

    abstract fun execute(params: Params? = null): T
}
