package com.olshevchenko.currencyconverter.core


data class Result<T> private constructor(
    val resultType: ResultType,
    val data: T? = null,
    val error: Exception? = null,
    val errorDesc: String? = null
) {

    companion object {
        fun <T> success(data: T?): Result<T> =
            Result(ResultType.SUCCESS, data)

        fun <T> error(error: Exception? = null, errorDesc: String?): Result<T> =
            Result(ResultType.ERROR, error = error, errorDesc = errorDesc)

        fun <T> errorWData(data: T?, error: Exception? = null, errorDesc: String?): Result<T> =
            Result(ResultType.ERROR_WITH_DATA, data, error = error, errorDesc = errorDesc)
    }

    enum class ResultType {
        SUCCESS,
        ERROR,
        ERROR_WITH_DATA, // e.g. network error from RatesRepositoryImpl with cache data instead
    }
}
