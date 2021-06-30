package com.olshevchenko.currencyconverter.features.converter.presentation

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.presentation.BasePresenter
import com.olshevchenko.currencyconverter.core.presentation.CodesViewState
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.converter.domain.usecase.GetCurrencyCodesUseCase
import io.reactivex.observers.DisposableSingleObserver


class CodesPresenter(
    private val getCurrencyCodesUseCase: GetCurrencyCodesUseCase,
    private val viewModel: ConverterViewModel,
) : BasePresenter(), ConverterContracts.CodesPresenter {

    private fun getCodesObserver() = object : DisposableSingleObserver<Result<CurrencyCodes>>() {
        override fun onSuccess(codesResult: Result<CurrencyCodes>) {
            parseCodesResult(codesResult)
        }

        override fun onError(e: Throwable) {
            e.printStackTrace() //ToDo add UI feedback here!!!
        }
    }

    override fun init() {
        viewModel.setCodesViewState(CodesViewState.init())
    }

    override fun onGetCodesAction() {
        getCurrencyCodesUseCase
            .execute(
                getCodesObserver(),
                { viewModel.setCodesViewState(CodesViewState.inProgress()) },
                { viewModel.setCodesViewState(CodesViewState.ready()) }
            )
    }

    override fun dispose() {
        getCurrencyCodesUseCase.dispose()
    }

    /**
     * parse getting result
     * change viewmodel data value & states depending on usecase execution result
     */
    fun parseCodesResult(codesResult: Result<CurrencyCodes>) {
        @Suppress("UNCHECKED_CAST")
        viewModel.setCodesViewState(
            with(codesResult) {
                when (this.resultType) {
                    Result.ResultType.SUCCESS ->
                        CodesViewState.success(this.data?.codes)
                    Result.ResultType.ERROR ->
                        CodesViewState.error(null, this.errorDesc)
                    Result.ResultType.ERROR_WITH_DATA ->
                        CodesViewState.error(this.data?.codes, this.errorDesc)
                }
            } as CodesViewState
        )
    }
}
