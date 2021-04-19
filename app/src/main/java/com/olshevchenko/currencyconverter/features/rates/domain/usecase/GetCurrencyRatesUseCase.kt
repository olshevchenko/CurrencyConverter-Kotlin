package com.olshevchenko.currencyconverter.features.rates.domain.usecase

import com.olshevchenko.currencyconverter.features.rates.domain.repository.CurrencyRatesRepository
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.core.usecase.RXUseCase
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRates
import io.reactivex.Scheduler
import io.reactivex.Single

class GetCurrencyRatesUseCase(
    private val ratesRepository: CurrencyRatesRepository,
    subscribeScheduler: Scheduler,
    private val postExecutionScheduler: Scheduler
) :
    RXUseCase<Result<CurrencyRates>, Unit>(subscribeScheduler, postExecutionScheduler) {
    override fun buildUseCaseSingle(params: Unit?): Single<Result<CurrencyRates>> =
        ratesRepository.getRates()
}
