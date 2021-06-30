package com.olshevchenko.currencyconverter.features.converter.presentation.model

import com.olshevchenko.currencyconverter.core.*
import kotlinx.serialization.Serializable

/**
 * UI-purposed result of executing refresh action with datetime and description if error
 */
@Serializable
data class RefreshResultUI(
    val result: Boolean,
    val dateTime: String,
    val errorDesc: String? = null,
)

object RefreshResultToUIMapper : BaseMapper<Result<Long>, RefreshResultUI> {
    override fun map(from: Result<Long>) =
        RefreshResultUI(
            result = when (from.resultType) {
                Result.ResultType.SUCCESS -> true
                else -> false
            },
            RefreshTimeStampToUIMapper.map(from.data),
            from.errorDesc,
        )
}

object RefreshTimeStampToUIMapper : BaseMapper<Long?, String> {
    override fun map(from: Long?): String =
        from?.let {
            it.toUIDateTime()
        } ?: CurrencyDateTime.UI_DATETIME_UNKNOWN
}