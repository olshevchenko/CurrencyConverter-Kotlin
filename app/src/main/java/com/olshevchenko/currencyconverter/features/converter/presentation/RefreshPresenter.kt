package com.olshevchenko.currencyconverter.features.converter.presentation

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.presentation.BasePresenter
import com.olshevchenko.currencyconverter.core.presentation.RefreshViewState
import com.olshevchenko.currencyconverter.features.converter.domain.usecase.RefreshCurrencyRatesUseCase
import com.olshevchenko.currencyconverter.features.converter.presentation.model.RefreshTimeStampToUIMapper
import io.reactivex.observers.DisposableSingleObserver

typealias RefreshResultTS = Result<Long> // rates refreshing timestamp

class RefreshPresenter(
    private val useCase: RefreshCurrencyRatesUseCase,
    private val viewModel: ConverterViewModel,
    private val refreshTimeStampToUIMapper: RefreshTimeStampToUIMapper,
) : BasePresenter(), ConverterContracts.RefreshPresenter {

    private fun getRefreshObserver() = object : DisposableSingleObserver<RefreshResultTS>() {
        override fun onSuccess(refreshResult: RefreshResultTS) {
            parseRefreshResult(refreshResult)
        }

        override fun onError(e: Throwable) {
            e.printStackTrace() //ToDo add UI feedback here!!!
        }
    }

    override fun init() {
        viewModel.setRefreshViewState(RefreshViewState.init())
        onRefreshRatesAction()
    }

    override fun onRefreshRatesAction() {
        useCase
            .execute(
                getRefreshObserver(),
                subscribeHandler = { viewModel.setRefreshViewState(RefreshViewState.inProgress()) },
                terminateHandler = { viewModel.setRefreshViewState(RefreshViewState.ready()) }
            )
    }

    override fun dispose() {
        useCase.dispose()
    }

    /**
     * parse getting result
     * change viewmodel data value & states depending on usecase execution result
     */
    fun parseRefreshResult(refreshResult: RefreshResultTS) {
        @Suppress("UNCHECKED_CAST")
        var refreshDateTime: String? = null
        viewModel.setRefreshViewState(
            with(refreshResult) {
                this.data?.let {
                    refreshDateTime = refreshTimeStampToUIMapper.map(it)
                }
                when (this.resultType) {
                    Result.ResultType.SUCCESS ->
                        RefreshViewState.success(refreshDateTime)
                    else -> // the same for ERROR, ERROR_WITH_DATA
                        RefreshViewState.error(refreshDateTime, this.errorDesc)
                }
            }
        )
    }
}
