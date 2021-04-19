package com.olshevchenko.currencyconverter.datasource.net.api

import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.UnknownHostException

class BadRequestException : Exception()

class CancelledFetchDataException : Exception()

class GenericNetworkException : Exception()

class NetworkConnectionException : Exception()

fun handleNetworkExceptions(ex: Exception): Exception {
    return when (ex) {
        is IOException -> NetworkConnectionException()
        is UnknownHostException -> NetworkConnectionException()
        is HttpException -> apiErrorFromCodeException(ex.code())
        else -> GenericNetworkException()
    }
}

private fun apiErrorFromCodeException(code: Int): Exception {
    return if (code == 400) {
        BadRequestException()
    } else {
        GenericNetworkException()
    }
}