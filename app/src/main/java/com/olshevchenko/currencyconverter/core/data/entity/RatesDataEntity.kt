package com.olshevchenko.currencyconverter.core.data.entity

import android.util.Log
import com.olshevchenko.currencyconverter.core.BaseMapper
import com.olshevchenko.currencyconverter.core.BaseParamMapper
import com.olshevchenko.currencyconverter.core.Utils
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.rates.domain.model.FromToCodes
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
        with(params) {
            CurrencyRate(this.from, this.to, quote.qValue, quote.qTimestamp.toString())
        }
}

object EntityToCurrencyRatesMapper : BaseMapper<RatesDataEntity, CurrencyRates> {
    override fun map(entity: RatesDataEntity): CurrencyRates {
        val ratesList = entity.quotes.toList().mapNotNull { pair->
            Utils.CurrencyCodes.split(pair.first)?.let { codes ->
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

//data class CurrencyRate(val baseCurrency: String, val quoteCurrency: String,
//                        val price: Double, val dateTime: String)
