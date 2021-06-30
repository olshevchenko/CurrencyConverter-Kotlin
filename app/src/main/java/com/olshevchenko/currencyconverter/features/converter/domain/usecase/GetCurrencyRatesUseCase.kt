package com.olshevchenko.currencyconverter.features.converter.domain.usecase

import com.olshevchenko.currencyconverter.features.converter.domain.repository.CurrencyRatesRepository
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.usecase.RXUseCase
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRates
import io.reactivex.Scheduler
import io.reactivex.Single

class GetCurrencyRatesUseCase(
    private val ratesRepository: CurrencyRatesRepository,
    subscribeScheduler: Scheduler,
    observeScheduler: Scheduler
) : RXUseCase<Result<CurrencyRates>, Unit>(subscribeScheduler, observeScheduler) {

    override fun buildUseCaseSingle(params: Unit?): Single<Result<CurrencyRates>> =
        ratesRepository.getRates()
}
