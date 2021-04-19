package com.olshevchenko.currencyconverter.datasource.cache

import android.util.Log
import com.olshevchenko.currencyconverter.core.data.entity.QuoteToCurrencyRateMapper
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.datasource.local.model.EntityToLocalMapper
import com.olshevchenko.currencyconverter.datasource.local.model.LocalToEntityMapper
import com.olshevchenko.currencyconverter.datasource.local.model.RatesLocal
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.rates.domain.model.FromToCodes
import com.olshevchenko.currencyconverter.core.Utils

/**
 * Local rates mirroring to RAM
 */
class RatesCacheDataSourceImpl(
    private val localToEntityMapper: LocalToEntityMapper,
    private val entityToLocalMapper: EntityToLocalMapper,
    private val quoteToCurrencyRateMapper: QuoteToCurrencyRateMapper,
) {

    /**
     * For quotes storage, use pure map inside
     * Wrap quotes in RatesDataEntity etc. only for requests processing
     */
    private var quotesCache = mutableMapOf<String, RatesDataEntity.Quote>()

    fun getRates(): RatesDataEntity = localToEntityMapper.map(RatesLocal(quotesCache))

    /**
     * merge OLD quotes w. new ones (overriding items if existed)
     */
    fun saveRates(ratesEntity: RatesDataEntity?) {
        ratesEntity?.let {
            quotesCache.putAll(
                (entityToLocalMapper.map(it)).quotes
            )
        }
    }

    fun getCodes(): CurrencyCodes {
        return CurrencyCodes(
            quotesCache.keys.mapNotNull { code ->
                Utils.CurrencyCodes.split(code)?.let {
                    it[1]
                } ?: run {
                    Log.w(
                        "RatesCacheDataSource",
                        "Got INCORRECT currency code pair (\"$code\") => skip it.."
                    )
                    null
                }
            }
        )
    }

    fun getRate(fromToCodes: FromToCodes?): CurrencyRate? {
         fromToCodes?.let {
             Utils.CurrencyCodes.merge(it.from, it.to)?.let { code ->
                quotesCache[code]?.let { quote->
                    return quoteToCurrencyRateMapper.map(quote, fromToCodes)
                } ?:
                    Log.w(
                        "RatesCacheDataSource",
                        "Rate for currency code pair (\"$fromToCodes\") NOT FOUND => return null.."
                    )
                return null
            } ?:
                Log.w(
                    "RatesCacheDataSource",
                    "Got INCORRECT currency code pair (\"$fromToCodes\") => return null.."
                )
                return null

        } ?:
            Log.w(
                "RatesCacheDataSource",
                "Got EMPTY currency code pair => return null.."
            )
            return null
    }
}
