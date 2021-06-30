package com.olshevchenko.currencyconverter.features.di

import com.olshevchenko.currencyconverter.features.converter.domain.repository.CurrencyRatesRepository
import com.olshevchenko.currencyconverter.features.converter.domain.usecase.GetCurrencyCodesUseCase
import com.olshevchenko.currencyconverter.features.converter.domain.usecase.GetCurrencyRateUseCase
import com.olshevchenko.currencyconverter.features.converter.domain.usecase.RefreshCurrencyRatesUseCase
import com.olshevchenko.currencyconverter.features.converter.domain.usecase.SaveCurrencyRatesUseCase
import com.olshevchenko.currencyconverter.features.converter.presentation.CodesPresenter
import com.olshevchenko.currencyconverter.features.converter.presentation.ConverterViewModel
import com.olshevchenko.currencyconverter.features.converter.presentation.RatePresenter
import com.olshevchenko.currencyconverter.features.converter.presentation.RefreshPresenter
import com.olshevchenko.currencyconverter.features.converter.presentation.model.RefreshTimeStampToUIMapper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


/**
 * File with presentation and business-layer Koin modules, common for all application features
 */

private val subscribeScheduler = Schedulers.io()
private val observeScheduler = AndroidSchedulers.mainThread()

/**
 * Koin modules for domain-level logic
 */
val useCaseModules = module {
    single(named("getCurrencyCodesUseCase")) {
        GetCurrencyCodesUseCase(
            ratesRepository = get<CurrencyRatesRepository>(named("ratesRepository")),
            subscribeScheduler,
            observeScheduler
        )
    }
    single(named("getCurrencyRateUseCase")) {
        GetCurrencyRateUseCase(
            ratesRepository = get<CurrencyRatesRepository>(named("ratesRepository")),
            subscribeScheduler,
            observeScheduler
        )
    }
    single(named("refreshCurrencyRatesUseCase")) {
        RefreshCurrencyRatesUseCase(
            ratesRepository = get<CurrencyRatesRepository>(named("ratesRepository")),
            subscribeScheduler,
            observeScheduler
        )
    }
    single(named("saveCurrencyRatesUseCase")) {
        SaveCurrencyRatesUseCase(
            ratesRepository = get<CurrencyRatesRepository>(named("ratesRepository")),
            subscribeScheduler,
            observeScheduler
        )
    }
}

/**
 * Koin modules for presentation-level (viewmodels & presenters)
 */
val presentationModules = module {
    viewModel(named("converterViewModel"))  {
        ConverterViewModel()
    }

    factory(named("codesPresenter")) {
        CodesPresenter(
            getCurrencyCodesUseCase = get<GetCurrencyCodesUseCase>(named("getCurrencyCodesUseCase")),
            viewModel = get<ConverterViewModel>(named("converterViewModel"))
        )
    }
    factory(named("ratePresenter")) {
        RatePresenter(
            getCurrencyRateUseCase = get<GetCurrencyRateUseCase>(named("getCurrencyRateUseCase")),
            viewModel = get<ConverterViewModel>(named("converterViewModel"))
        )
    }
    factory(named("refreshPresenter")) {
        RefreshPresenter(
            useCase = get<RefreshCurrencyRatesUseCase>(named("refreshCurrencyRatesUseCase")),
            viewModel = get<ConverterViewModel>(named("converterViewModel")),
            RefreshTimeStampToUIMapper
        )
    }
}

val homeModules = listOf(
    useCaseModules,
    presentationModules,
)
