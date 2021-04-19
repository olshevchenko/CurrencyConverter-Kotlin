package com.olshevchenko.currencyconverter.datasource

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import io.reactivex.Single

interface RatesDataSource {
    fun getRates(): Single<Result<RatesDataEntity>>
}