package com.olshevchenko.currencyconverter.features.rates.domain.usecase

import com.olshevchenko.currencyconverter.features.rates.domain.repository.CurrencyRatesRepository
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.usecase.RXUseCase
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyCodes
import io.reactivex.Scheduler
import io.reactivex.Single

class GetCurrencyCodesUseCase(
    private val ratesRepository: CurrencyRatesRepository,
    subscribeScheduler: Scheduler,
    private val postExecutionScheduler: Scheduler
) :
    RXUseCase<Result<CurrencyCodes>, Unit>(subscribeScheduler, postExecutionScheduler) {
    override fun buildUseCaseSingle(params: Unit?): Single<Result<CurrencyCodes>> =
        ratesRepository.getCodes()
}
