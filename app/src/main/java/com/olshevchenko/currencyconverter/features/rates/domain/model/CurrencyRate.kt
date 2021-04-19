package com.olshevchenko.currencyconverter.features.rates.domain.model

/**
 * Price of ONE [baseCurrency] unit denominated in amount of [quoteCurrency] units on a certain [dateTime]
 */
data class CurrencyRate(val baseCurrency: String, val quoteCurrency: String,
                        val price: Double, val dateTime: String)

/**
 * Simple pair of 3-character currency ("From-" & "To-") codes
 */
data class FromToCodes(val from: String, val to: String)