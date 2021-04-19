package com.olshevchenko.currencyconverter.datasource.net.model

import com.olshevchenko.currencyconverter.core.BaseMapper
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity

data class RatesApi(
    val success: Boolean,
//    @Json(name = "terms")
//    @Json(name = "privacy")
    val error: ErrorDesc?,
    val timestamp: Long?,
    val source: String?,
    val quotes: Map<String, Double>?
) {
    data class ErrorDesc(val code: Int, val info: String)

    override fun toString() =
        "{\n" +
                "  \"success\": $success,\n" +
            if (! success) { // error response
                "  \"error\":{\n" +
                "    \"code\": ${error!!.code},\n" +
                "    \"info\": ${error.info},\n"
            } else { // success response
                "  \"timestamp\": ${timestamp!!},\n" +
                "  \"source\": ${source!!},\n" +
                "  \"quotes\":{\n" +
                buildString {
                    quotes!!.forEach {
                        append(
                            "    \"${it.key}\":${it.value},\n"
                        )
                    }
                }
            } +
                "  }\n" +
                "}\n"
}

/**
 * Convert data from API- into DataEntity- format
 */
object ApiToEntityMapper : BaseMapper<RatesApi, RatesDataEntity> {
    override fun map(from: RatesApi): RatesDataEntity {
        val quotes: MutableMap<String, RatesDataEntity.Quote> = mutableMapOf()
        from.quotes?.forEach {
            quotes[it.key] = RatesDataEntity.Quote(from.timestamp!!, it.value)
        }
        return RatesDataEntity(quotes)
    }
}