package com.olshevchenko.currencyconverter.features.di

import com.olshevchenko.currencyconverter.features.rates.domain.usecase.GetCurrencyCodesUseCase
import com.olshevchenko.currencyconverter.features.rates.domain.usecase.GetCurrencyRateUseCase
import com.olshevchenko.currencyconverter.features.rates.domain.usecase.GetCurrencyRatesUseCase
import com.olshevchenko.currencyconverter.features.rates.domain.usecase.SaveCurrencyRatesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module


private val subscribeScheduler = Schedulers.io()
private val postExecutionScheduler = AndroidSchedulers.mainThread()

val homeModule = module {
    factory {
        GetCurrencyCodesUseCase(
            ratesRepository = get(),
            subscribeScheduler,
            postExecutionScheduler
        )
    }
    factory {
        GetCurrencyRateUseCase(
            ratesRepository = get(),
            subscribeScheduler,
            postExecutionScheduler
        )
    }
    factory {
        GetCurrencyRatesUseCase(
            ratesRepository = get(),
            subscribeScheduler,
            postExecutionScheduler
        )
    }
    factory {
        SaveCurrencyRatesUseCase(
            ratesRepository = get(),
            subscribeScheduler,
            postExecutionScheduler
        )
    }
}