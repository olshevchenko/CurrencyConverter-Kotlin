package com.olshevchenko.currencyconverter.features.operations.domain.model

/**
 * Exchange of [fromAmount] units of [fromCurrency] into [toAmount] units of [toCurrency] on a certain [dateTime]
 */
data class ExchangeOperation(
        val id: Int,
        val fromCurrency: String, val toCurrency: String,
        val fromAmount: Double, val toAmount: Double,
        val dateTime: String
        )
