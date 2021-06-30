package com.olshevchenko.currencyconverter.features.converter.presentation

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.presentation.BasePresenter
import com.olshevchenko.currencyconverter.core.presentation.RateViewState
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.converter.domain.model.FromToCodes
import com.olshevchenko.currencyconverter.features.converter.domain.usecase.GetCurrencyRateUseCase
import io.reactivex.observers.DisposableSingleObserver


class RatePresenter(
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val viewModel: ConverterViewModel,
) : BasePresenter(), ConverterContracts.RatePresenter {

    private fun getRateObserver() = object : DisposableSingleObserver<Result<CurrencyRate>>() {
        override fun onSuccess(rateResult: Result<CurrencyRate>) {
            parseRateResult(rateResult)
        }

        override fun onError(e: Throwable) {
            e.printStackTrace() //ToDo add UI feedback here!!!
        }
    }

    override fun init() {
        viewModel.setRateViewState(RateViewState.init())
    }

    override fun onGetRateAction(from: String, to: String) {
        getCurrencyRateUseCase
            .execute(
                getRateObserver(),
                { viewModel.setRateViewState(RateViewState.inProgress()) },
                { viewModel.setRateViewState(RateViewState.ready()) },
                FromToCodes(from, to)
            )
    }

    override fun dispose() {
        getCurrencyRateUseCase.dispose()
    }

    /**
     * parse getting result
     * change viewmodel data value & states depending on usecase execution result
     */
    fun parseRateResult(rateResult: Result<CurrencyRate>) {
        @Suppress("UNCHECKED_CAST")
        viewModel.setRateViewState(
            with(rateResult) {
                when (this.resultType) {
                    Result.ResultType.SUCCESS ->
                        RateViewState.success(this.data)
                    Result.ResultType.ERROR ->
                        RateViewState.error(null, this.errorDesc)
                    Result.ResultType.ERROR_WITH_DATA ->
                        RateViewState.error(this.data, this.errorDesc)
                }
            } as RateViewState
        )
    }
}