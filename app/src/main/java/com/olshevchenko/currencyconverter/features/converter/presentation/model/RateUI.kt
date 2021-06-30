package com.olshevchenko.currencyconverter.features.converter.presentation.model

import com.olshevchenko.currencyconverter.core.BaseMapper
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import kotlinx.serialization.Serializable

/**
 * UI-purposed price of ONE baseCurrency unit denominated in amount of quoteCurrency units
 */
@Serializable
data class RateUI(val price: Double)

object CurrencyRateToUIMapper : BaseMapper<CurrencyRate, RateUI> {
    override fun map(from: CurrencyRate): RateUI = RateUI(from.price)
}
