package com.olshevchenko.currencyconverter.core.data

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.entity.EntityToCurrencyRatesMapper
import com.olshevchenko.currencyconverter.datasource.cache.RatesCacheDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.local.RatesLocalDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.net.RatesNetworkDataSourceImpl
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.converter.domain.model.FromToCodes
import com.olshevchenko.currencyconverter.features.converter.domain.repository.CurrencyRatesRepository
import io.reactivex.Single

/**
 * Implementation of [CurrencyRatesRepository] interface
 *
 * @param rates_DataSourceImpl - set of different data repositories (api-, cache-, local file-)
 * @param entityToCurrencyRatesMapper -  Entity <=> Domain-level CurrencyRate mapper
 */
class RatesRepositoryImpl(
    private val ratesNetworkDataSourceImpl: RatesNetworkDataSourceImpl,
    private val ratesCacheDataSourceImpl: RatesCacheDataSourceImpl,
    private val ratesLocalDataSourceImpl: RatesLocalDataSourceImpl,
    private val entityToCurrencyRatesMapper: EntityToCurrencyRatesMapper,
) : CurrencyRatesRepository {

    init {
        // initially, load rates from prev. session (locally saved) into RAM cache
        ratesCacheDataSourceImpl.saveRates(ratesLocalDataSourceImpl.loadRates())
    }

    /**
     * CURRENT rates list updated in [refreshRates]
     */
    private var ratesRecent = ratesCacheDataSourceImpl.getRates() // at 1'st, fill rates from cache

    /**
     * Get existed (!!!) currency rates from cache.
     * Rates have to be saved there preliminarily by calling [refreshRates]
     * @return "Success" result (w. cache data) converting 'DataEntity' to
     * domain-level 'CurrencyRates' previously - if ones exist
     * or "Error" if cache is empty yet
     */
    override fun getRates(): Single<Result<CurrencyRates>> {
        return with(ratesRecent) {
            if (this.quotes.isEmpty())
                Single.just(
                    Result.errorWData(
                        entityToCurrencyRatesMapper.map(this), // anyway, return empty list
                        errorDesc = "Existed currency rates list is empty"
                    )
                )
            else
                Single.just(
                    Result.success(
                        entityToCurrencyRatesMapper.map(this)
                    )
                )
        }
    }

    /**
     * Save actual currency rates into local (file-based) repository
     */
    override fun saveRates(): Single<Result<Unit>> {
        ratesLocalDataSourceImpl.saveRates(ratesRecent) // ignore possible problems
        return Single.just(Result.success())
    }

    /**
     * Get new / refresh existed currency rates from network and save them in cache if succeded
     * @return "Success" result + updated timestamp or
     * "Error" with description depending on operation results
     */
    override fun refreshRates(): Single<Result<Long>> {
        return ratesNetworkDataSourceImpl.getRates().map { ratesResponse ->
            if (ratesResponse.resultType == Result.ResultType.SUCCESS) {
                ratesResponse.data?.let {
                    // update cache
                    ratesCacheDataSourceImpl.saveRates(it)
                    ratesRecent = ratesCacheDataSourceImpl.getRates()
                }
                Result.success(
                    ratesCacheDataSourceImpl.getRatesTimestamp()
                )
            } else with(ratesResponse) {
                Result.errorWData(
                    ratesCacheDataSourceImpl.getRatesTimestamp(),
                    this.error,
                    this.errorDesc
                )
            }
        }
    }

    override fun getCodes(): Single<Result<CurrencyCodes>> =
        Single.just(Result.success(ratesCacheDataSourceImpl.getCodes()))

    override fun getRate(fromToCodes: FromToCodes?): Single<Result<CurrencyRate>> =
        ratesCacheDataSourceImpl.getRate(fromToCodes)?.let {
            Single.just(Result.success(it))
        } ?: Single.just(Result.error(errorDesc = "Rate NOT FOUND"))
}