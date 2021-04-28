package com.olshevchenko.currencyconverter.features.di

import com.olshevchenko.currencyconverter.features.rates.domain.usecase.GetCurrencyCodesUseCase
import com.olshevchenko.currencyconverter.features.rates.domain.usecase.GetCurrencyRateUseCase
import com.olshevchenko.currencyconverter.features.rates.domain.usecase.GetCurrencyRatesUseCase
import com.olshevchenko.currencyconverter.features.rates.domain.usecase.SaveCurrencyRatesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.dsl.module


/**
 * File with business/interactors-layer Koin modules, common for all application features
 */


private val subscribeScheduler = Schedulers.io()
private val observeScheduler = AndroidSchedulers.mainThread()

val homeModule = module {
    factory {
        GetCurrencyCodesUseCase(
            ratesRepository = get(),
            subscribeScheduler,
            observeScheduler
        )
    }
    factory {
        GetCurrencyRateUseCase(
            ratesRepository = get(),
            subscribeScheduler,
            observeScheduler
        )
    }
    factory {
        GetCurrencyRatesUseCase(
            ratesRepository = get(),
            subscribeScheduler,
            observeScheduler
        )
    }
    factory {
        SaveCurrencyRatesUseCase(
            ratesRepository = get(),
            subscribeScheduler,
            observeScheduler
        )
    }
}
