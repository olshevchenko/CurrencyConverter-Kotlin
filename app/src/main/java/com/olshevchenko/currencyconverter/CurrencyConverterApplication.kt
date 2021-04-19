package com.olshevchenko.currencyconverter

import android.app.Application
import com.olshevchenko.currencyconverter.datasource.di.*
import com.olshevchenko.currencyconverter.features.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CurrencyConverterApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@CurrencyConverterApplication)
            androidLogger()
            modules(
                listOf(
                    retrofitModule,
                    networkModules,
                    datasourceModules,
                    repositoryModules,
                    homeModule
                )
            )
        }
    }
}
