package com.olshevchenko.currencyconverter.features.converter.domain.repository

import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.converter.domain.model.FromToCodes
import io.reactivex.Single

interface CurrencyRatesRepository {

    fun getRates(): Single<Result<CurrencyRates>>

    fun saveRates(): Single<Result<Unit>>

    fun refreshRates(): Single<Result<Long>>

    fun getCodes(): Single<Result<CurrencyCodes>>

    fun getRate(fromToCodes: FromToCodes?): Single<Result<CurrencyRate>>

//    fun clear(): Single<Result<Unit>> //TODO kind of "completely clear current currencies rates" usecase
}