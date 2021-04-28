package com.olshevchenko.currencyconverter.features.rates.presentation

class RatesListView : RatesContract.View {

    private var ratesPresenter: RatesContract.Presenter? = null

    override fun attachPresenter(presenter: RatesContract.Presenter) {
        ratesPresenter = presenter
    }

    override fun initView() {}

    override fun setViewState(viewState: RatesViewState) {}
}