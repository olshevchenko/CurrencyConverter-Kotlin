package com.olshevchenko.currencyconverter.core.ui


data class ViewState<T> private constructor(
    val stateType: StateType,
    val data: T? = null,
    val error: Exception? = null,
    val errorDesc: String? = null
) {

    companion object {
        fun <T> init(): ViewState<T> =
            ViewState(StateType.INIT)

        fun <T> loading(): ViewState<T> =
            ViewState(StateType.LOADING)

        fun <T> loadingFinished(): ViewState<T> =
            ViewState(StateType.LOADING_FINISHED)

        fun <T> success(data: T?): ViewState<T> =
            ViewState(StateType.SUCCESS, data)

        fun <T> error(data: T? = null, error: Exception? = null, errorDesc: String?): ViewState<T> =
            ViewState(StateType.ERROR, data, error = error, errorDesc = errorDesc)
    }

    enum class StateType {
        INIT,
        LOADING,
        LOADING_FINISHED,
        SUCCESS,
        ERROR,
    }
}
