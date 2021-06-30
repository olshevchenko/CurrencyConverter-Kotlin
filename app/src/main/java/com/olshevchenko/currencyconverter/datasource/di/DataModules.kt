package com.olshevchenko.currencyconverter.datasource.di

import android.content.Context
import com.olshevchenko.currencyconverter.core.data.RatesRepositoryImpl
import com.olshevchenko.currencyconverter.core.data.entity.EntityToCurrencyRatesMapper
import com.olshevchenko.currencyconverter.core.data.entity.QuoteToCurrencyRateMapper
import com.olshevchenko.currencyconverter.datasource.cache.RatesCacheDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.local.RatesLocalDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.local.model.EntityToLocalMapper
import com.olshevchenko.currencyconverter.datasource.local.model.LocalToEntityMapper
import com.olshevchenko.currencyconverter.datasource.net.RatesNetworkDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.net.api.RatesApiService
import com.olshevchenko.currencyconverter.datasource.net.model.ApiToEntityMapper
import com.olshevchenko.currencyconverter.features.di.presentationModules
import com.olshevchenko.currencyconverter.features.di.useCaseModules
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.schedulers.Schedulers
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File


/**
 * File with datasource-layer Koin modules, common for all application features
 */


private const val API_BASE_URL = "http://apilayer.net/api/"
private const val LOCAL_RATES_STORAGE = "SavedRates.txt"


/**
 * Build the Moshi object for Retrofit, adding the Kotlin adapter
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private fun provideRetrofitInstance(): Retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
    .baseUrl(API_BASE_URL)
    .build()

private fun provideRatesApiService(retrofit: Retrofit): RatesApiService =
    retrofit.create(RatesApiService::class.java)


/**
 * Koin modules for network API
 */
val networkModules = module {
    single(named("retrofit")) {
        provideRetrofitInstance()
    }
    factory(named("ratesApiService")) {
        provideRatesApiService(
            retrofit = get<Retrofit>(
                named("retrofit")
            )
        )
    }
}

private fun provideLocalRatesFile(context: Context): File {
    val filePath: String = context.filesDir.path.toString() + "/$LOCAL_RATES_STORAGE"
    return File(filePath)
}

/**
 * Koin modules for all types of data sources
 */
val dataSourceModules = module {
    single(named("ratesNetworkDataSourceImpl")) {
        RatesNetworkDataSourceImpl(
            ratesApiService = get<RatesApiService>(
                named("ratesApiService")
            ),
            ApiToEntityMapper
        )
    }
    single(named("ratesCacheDataSourceImpl")) {
        RatesCacheDataSourceImpl(
            LocalToEntityMapper,
            EntityToLocalMapper,
            QuoteToCurrencyRateMapper,
        )
    }
    single(named("ratesLocalDataSourceImpl")) {
        RatesLocalDataSourceImpl(
            file = provideLocalRatesFile(
                context = get()
            ),
            LocalToEntityMapper,
            EntityToLocalMapper
        )
    }
}

/**
 * Koin modules for repositories implementation
 */
val repositoryModules = module {
    single (named("ratesRepository")) {
        RatesRepositoryImpl(
            ratesNetworkDataSourceImpl = get<RatesNetworkDataSourceImpl>(
                named("ratesNetworkDataSourceImpl")
            ),
            ratesCacheDataSourceImpl = get<RatesCacheDataSourceImpl>(
                named("ratesCacheDataSourceImpl")
            ),
            ratesLocalDataSourceImpl = get<RatesLocalDataSourceImpl>(
                named("ratesLocalDataSourceImpl")
            ),
            EntityToCurrencyRatesMapper
        )
    }
}

val dataModules = listOf(
    networkModules,
    dataSourceModules,
    repositoryModules,
)
