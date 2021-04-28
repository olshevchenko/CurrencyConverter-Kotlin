package com.olshevchenko.currencyconverter.features.rates.presentation

import com.olshevchenko.currencyconverter.core.ui.ViewState
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRate

typealias RatesViewState = ViewState<List<CurrencyRate>>

interface RatesContract {
    interface View {
        fun attachPresenter(presenter: Presenter)
        fun initView()
        fun setViewState(viewState: RatesViewState)
    }

    interface Presenter {
        fun attachView(view: RatesContract.View)
        fun init()
        fun getRates()
        fun dispose()
    }
}