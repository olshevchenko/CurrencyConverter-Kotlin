package com.olshevchenko.currencyconverter.core.data.entity

import android.util.Log
import com.olshevchenko.currencyconverter.core.BaseMapper
import com.olshevchenko.currencyconverter.core.BaseParamMapper
import com.olshevchenko.currencyconverter.core.splitBy3
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.converter.domain.model.FromToCodes
import kotlinx.serialization.Serializable

/**
 * Currency Rates Entity used in the data layer
 */
data class RatesDataEntity(val quotes: Map<String, Quote>) {
    @Serializable
    data class Quote(val qTimestamp: Long, val qValue: Double)
}


object QuoteToCurrencyRateMapper : BaseParamMapper<RatesDataEntity.Quote, CurrencyRate, FromToCodes> {
    override fun map(quote: RatesDataEntity.Quote, params: FromToCodes): CurrencyRate =
//        with(params) {
//            CurrencyRate(this.from, this.to, quote.qValue, quote.qTimestamp.toString())
//        }
        CurrencyRate(params.from, params.to, quote.qValue, quote.qTimestamp.toString())
}

object EntityToCurrencyRatesMapper : BaseMapper<RatesDataEntity, CurrencyRates> {
    override fun map(entity: RatesDataEntity): CurrencyRates {
        val ratesList = entity.quotes.toList().mapNotNull { pair->
            pair.first.splitBy3()?.let { codes ->
                QuoteToCurrencyRateMapper.map(pair.second, FromToCodes(codes[0], codes[1]))
            } ?: run {
                Log.w(
                    "Data2CurrencyRatesMap..",
                    "Got INCORRECT currency code pair (\"${pair.first}\") => skip it.."
                )
                null
            }
        }
        return CurrencyRates(ratesList)
    }
}