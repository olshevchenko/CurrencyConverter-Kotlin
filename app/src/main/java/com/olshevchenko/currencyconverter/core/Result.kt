package com.olshevchenko.currencyconverter.core


data class Result<T> private constructor(
    val resultType: ResultType,
    val data: T? = null,
    val error: Exception? = null,
    val errorDesc: String? = null
) {

    companion object {
        fun <T> success(data: T? = null): Result<T> =
            Result(ResultType.SUCCESS, data)

        fun <T> error(error: Exception? = null, errorDesc: String?): Result<T> =
            Result(ResultType.ERROR, data = null, error = error, errorDesc = errorDesc)

        fun <T> errorWData(data: T?, error: Exception? = null, errorDesc: String?): Result<T> =
            Result(ResultType.ERROR_WITH_DATA, data, error = error, errorDesc = errorDesc)
    }

    enum class ErrorType {
        SERVER, // network-caused errors on server side
        CLIENT, // errors made by the client
        NETWORK_CONNECTION, // any connection-caused errors
        LOCAL, // errors with local/cache repositories, etc.
    }

    enum class ResultType {
        SUCCESS,
        ERROR,
        ERROR_WITH_DATA, // e.g. network error from RatesRepositoryImpl with cache data instead
    }
}
