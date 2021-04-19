package com.olshevchenko.currencyconverter.features.rates.domain.repository

import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.rates.domain.model.FromToCodes
import io.reactivex.Single

interface CurrencyRatesRepository {

    fun getRates(): Single<Result<CurrencyRates>>

    fun saveRates(): Single<Result<Unit>>

    fun getCodes(): Single<Result<CurrencyCodes>>

    fun getRate(fromToCodes: FromToCodes?): Single<Result<CurrencyRate>>

//    fun clear(): Single<Result<Unit>> //TODO kind of "completely clear current currencies rates" usecase
}