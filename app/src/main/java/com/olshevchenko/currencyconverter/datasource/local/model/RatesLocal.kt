package com.olshevchenko.currencyconverter.datasource.local.model

import com.olshevchenko.currencyconverter.core.BaseMapper
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity

import kotlinx.serialization.Serializable

@Serializable
data class RatesLocal(var quotes: Map<String, RatesDataEntity.Quote>)

object LocalToEntityMapper : BaseMapper<RatesLocal, RatesDataEntity> {
    override fun map(from: RatesLocal): RatesDataEntity = RatesDataEntity(from.quotes)
}

object EntityToLocalMapper : BaseMapper<RatesDataEntity, RatesLocal> {
    override fun map(from: RatesDataEntity): RatesLocal = RatesLocal(from.quotes)
}
