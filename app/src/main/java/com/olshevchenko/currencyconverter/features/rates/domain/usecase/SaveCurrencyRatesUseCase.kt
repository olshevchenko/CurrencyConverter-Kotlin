package com.olshevchenko.currencyconverter.features.rates.domain.usecase

import com.olshevchenko.currencyconverter.features.rates.domain.repository.CurrencyRatesRepository
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.usecase.RXUseCase
import io.reactivex.Scheduler
import io.reactivex.Single

class SaveCurrencyRatesUseCase(
    private val ratesRepository: CurrencyRatesRepository,
    subscribeScheduler: Scheduler,
    private val postExecutionScheduler: Scheduler
) :
    RXUseCase<Result<Unit>, Unit>(subscribeScheduler, postExecutionScheduler) {
    override fun buildUseCaseSingle(params: Unit?): Single<Result<Unit>> =
        ratesRepository.saveRates()
}
