package com.olshevchenko.currencyconverter.core.data

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.entity.EntityToCurrencyRatesMapper
import com.olshevchenko.currencyconverter.datasource.cache.RatesCacheDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.local.RatesLocalDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.net.RatesNetworkDataSourceImpl
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.rates.domain.model.FromToCodes
import com.olshevchenko.currencyconverter.features.rates.domain.repository.CurrencyRatesRepository
import io.reactivex.Single

/**
 * Implementation of [CurrencyRatesRepository] interface
 *
 * @param dataFactory - A factory to construct different data source implementations
 * @param dataMapper -  Entity <=> Data CurrencyRate mapper
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

    private var ratesRecent = ratesCacheDataSourceImpl.getRates() // at 1'st, fill rates from cache

    /**
     * Get new / refresh existed currency rates from network and save them in cache if succeded
     * @return "Success" result (w. updated cache data) or "Error" with old one (converting
     * 'DataEntity' to domain-level 'CurrencyRates' previously)
     */
    override fun getRates(): Single<Result<CurrencyRates>> {
        return ratesNetworkDataSourceImpl.getRates().map { ratesResponse ->
            if (ratesResponse.resultType == Result.ResultType.SUCCESS) {
                ratesResponse.data?.let {
                    // update cache
                    ratesCacheDataSourceImpl.saveRates(it)
                    ratesRecent = ratesCacheDataSourceImpl.getRates()
                }
                Result.success(
                    entityToCurrencyRatesMapper.map(ratesRecent)
                )
            } else with(ratesResponse) {
                // return old cache
                Result.errorWData(
                    entityToCurrencyRatesMapper.map(ratesRecent),
                    this.error, this.errorDesc)
            }
        }
    }

    /**
     * Save actual currency rates into local (file-based) repository
     */
    override fun saveRates(): Single<Result<Unit>> {
        ratesLocalDataSourceImpl.saveRates(ratesRecent) // ignore possible problems
        return Single.just(Result.success(Unit))
    }

    override fun getCodes(): Single<Result<CurrencyCodes>> =
        Single.just(Result.success(ratesCacheDataSourceImpl.getCodes()))

    override fun getRate(fromToCodes: FromToCodes?): Single<Result<CurrencyRate>> =
        ratesCacheDataSourceImpl.getRate(fromToCodes)?.let {
            Single.just(Result.success(it))
        } ?:
            Single.just(Result.error(errorDesc = "Rate NOT FOUND"))
}