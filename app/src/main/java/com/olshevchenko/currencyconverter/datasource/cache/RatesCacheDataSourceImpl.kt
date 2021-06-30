package com.olshevchenko.currencyconverter.datasource.cache

import android.util.Log
import com.olshevchenko.currencyconverter.core.mergeTo6
import com.olshevchenko.currencyconverter.core.splitBy3
import com.olshevchenko.currencyconverter.core.data.entity.QuoteToCurrencyRateMapper
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.datasource.local.model.EntityToLocalMapper
import com.olshevchenko.currencyconverter.datasource.local.model.LocalToEntityMapper
import com.olshevchenko.currencyconverter.datasource.local.model.RatesLocal
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.converter.domain.model.FromToCodes

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

    /**
     * timestamp of recent quotes in cache
     */
    private var quotesTimestamp: Long = 0

    fun getRates(): RatesDataEntity = localToEntityMapper.map(RatesLocal(quotesCache))

    fun getRatesTimestamp(): Long = quotesTimestamp

    /**
     * merge OLD quotes w. new ones (overriding items if existed)
     * reset old timestamp with new non-empty one
     */
    fun saveRates(ratesEntity: RatesDataEntity?) {
        ratesEntity?.let {
            quotesCache.putAll(
                (entityToLocalMapper.map(it)).quotes
            )
            with(it.quotes.values) {
                if (this.isNotEmpty())
                    quotesTimestamp = this.elementAt(0).qTimestamp
            }
        }
    }

    fun getCodes(): CurrencyCodes {
        return CurrencyCodes(
            quotesCache.keys.mapNotNull { code ->
                code.splitBy3()?.let {
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
            it.mergeTo6()?.let { code ->
                quotesCache[code]?.let { quote ->
                    return quoteToCurrencyRateMapper.map(quote, fromToCodes)
                } ?: Log.w(
                    "RatesCacheDataSource",
                    "Rate for currency code pair (\"$fromToCodes\") NOT FOUND => return null.."
                )
                return null
            } ?: Log.w(
                "RatesCacheDataSource",
                "Got INCORRECT currency code pair (\"$fromToCodes\") => return null.."
            )
            return null

        } ?: Log.w(
            "RatesCacheDataSource",
            "Got EMPTY currency code pair => return null.."
        )
        return null
    }
}
