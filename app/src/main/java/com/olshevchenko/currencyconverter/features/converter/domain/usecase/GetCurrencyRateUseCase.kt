package com.olshevchenko.currencyconverter.features.converter.domain.usecase

import com.olshevchenko.currencyconverter.features.converter.domain.repository.CurrencyRatesRepository
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.usecase.RXUseCase
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.converter.domain.model.FromToCodes
import io.reactivex.Scheduler
import io.reactivex.Single

class GetCurrencyRateUseCase(
    private val ratesRepository: CurrencyRatesRepository,
    subscribeScheduler: Scheduler,
    observeScheduler: Scheduler
) : RXUseCase<Result<CurrencyRate>, FromToCodes>(subscribeScheduler, observeScheduler) {

    override fun buildUseCaseSingle(params: FromToCodes?): Single<Result<CurrencyRate>> =
        ratesRepository.getRate(params)
}
