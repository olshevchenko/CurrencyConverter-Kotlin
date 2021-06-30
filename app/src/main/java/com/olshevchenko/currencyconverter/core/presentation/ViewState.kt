package com.olshevchenko.currencyconverter.core.presentation

import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate


typealias CodesViewState = ViewState<CurrencyCodes>
typealias RateViewState = ViewState<CurrencyRate>
typealias RefreshViewState = ViewState<String>

data class ViewState<T> private constructor(
    val stateType: StateType,
    val data: T? = null,
//    val error: Exception? = null,
    val errorDesc: String? = null
) {

    companion object {
        fun <T> init(): ViewState<T> =
            ViewState(StateType.INIT)

        fun <T> inProgress(): ViewState<T> =
            ViewState(StateType.IN_PROGRESS)

        fun <T> ready(): ViewState<T> =
            ViewState(StateType.READY)

        fun <T> success(data: T?): ViewState<T> =
            ViewState(StateType.SUCCESS, data)

        fun <T> error(data: T? = null, errorDesc: String?): ViewState<T> =
            ViewState(StateType.ERROR, data, errorDesc = errorDesc)
    }

    enum class StateType {
        INIT,
        IN_PROGRESS,
        READY,
        SUCCESS,
        ERROR,
    }
}
