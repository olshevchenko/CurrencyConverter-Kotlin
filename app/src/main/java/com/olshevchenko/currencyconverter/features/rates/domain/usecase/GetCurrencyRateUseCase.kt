package com.olshevchenko.currencyconverter.features.rates.domain.usecase

import com.olshevchenko.currencyconverter.features.rates.domain.repository.CurrencyRatesRepository
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.usecase.RXUseCase
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.rates.domain.model.FromToCodes
import io.reactivex.Scheduler
import io.reactivex.Single

class GetCurrencyRateUseCase(
    private val ratesRepository: CurrencyRatesRepository,
    subscribeScheduler: Scheduler,
    postExecutionScheduler: Scheduler
) :
    RXUseCase<Result<CurrencyRate>, FromToCodes>(subscribeScheduler, postExecutionScheduler) {

    override fun buildUseCaseSingle(params: FromToCodes?): Single<Result<CurrencyRate>> =
        ratesRepository.getRate(params)
}
