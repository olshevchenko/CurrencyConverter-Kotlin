package com.olshevchenko.currencyconverter.features.converter.presentation.model

import com.olshevchenko.currencyconverter.core.BaseMapper
import com.olshevchenko.currencyconverter.core.BaseParamMapper
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyCodes
import kotlinx.serialization.Serializable

/**
 * UI-purposed currency codes list currently identical with domain- layer codes
 */
@Serializable
data class CodesUI(
val result: Boolean,
val codes: List<String>,
val errorDesc: String? = null,
)

object CurrencyCodesToUIMapper : BaseMapper<Result<CurrencyCodes>, CodesUI> {
    override fun map(from: Result<CurrencyCodes>) =
        CodesUI(
            result = when (from.resultType) {
                Result.ResultType.SUCCESS -> true
                else -> false
            },
            codes = from.data?.codes?: listOf(),
            from.errorDesc,
        )
}
