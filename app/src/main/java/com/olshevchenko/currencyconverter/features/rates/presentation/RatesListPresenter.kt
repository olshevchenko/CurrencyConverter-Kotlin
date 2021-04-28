package com.olshevchenko.currencyconverter.features.rates.presentation

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.rates.domain.usecase.GetCurrencyRatesUseCase
import io.reactivex.observers.DisposableSingleObserver


class RatesListPresenter(
    private val getCurrencyRatesUseCase: GetCurrencyRatesUseCase,
) : RatesContract.Presenter {

    private var ratesView: RatesContract.View? = null

    private val ratesObserver = object : DisposableSingleObserver<Result<CurrencyRates>>() {
        override fun onSuccess(ratesResult: Result<CurrencyRates>) {
            parseRatesResult(ratesResult)
        }
        override fun onError(e: Throwable) {
            e.printStackTrace() //ToDo add UI feedback here!!!
        }
    }


    override fun init() {
        ratesView?.initView()
        getRates()
    }

    override fun attachView(view: RatesContract.View) {
        ratesView = view
    }

    override fun getRates() {
        getCurrencyRatesUseCase
            .execute(
                ratesObserver,
                { ratesView?.setViewState(RatesViewState.loading()) },
                { ratesView?.setViewState(RatesViewState.loadingFinished()) }
            )
    }

    override fun dispose() {
        getCurrencyRatesUseCase.dispose()
    }

    /**
     * parse getting result
     * show rates / rates with error message / single (severe) error, depending on result details
     */
    fun parseRatesResult(ratesResult: Result<CurrencyRates>) {
        ratesView?.setViewState(
            with(ratesResult) {
                when (this.resultType) {
                    Result.ResultType.SUCCESS ->
                        RatesViewState.success(this.data?.rates)
                    Result.ResultType.ERROR ->
                        RatesViewState.error(null, this.error, this.errorDesc)
                    Result.ResultType.ERROR_WITH_DATA ->
                        RatesViewState.error(this.data?.rates, this.error, this.errorDesc)
                }
            }
        )
    }
}