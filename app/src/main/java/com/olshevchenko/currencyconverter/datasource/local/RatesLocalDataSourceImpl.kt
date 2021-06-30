package com.olshevchenko.currencyconverter.datasource.local

import android.util.Log
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.datasource.local.model.EntityToLocalMapper
import com.olshevchenko.currencyconverter.datasource.local.model.LocalToEntityMapper
import com.olshevchenko.currencyconverter.datasource.local.model.RatesLocal
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * Local rates saved to & loaded from local file
 * ToDo !!! Need to have preliminary prepared local file with single "USDUSD" currency rate
 */
class RatesLocalDataSourceImpl(
    private val file: File,
    private val localToEntityMapper: LocalToEntityMapper,
    private val entityToLocalMapper: EntityToLocalMapper
) {

    /**
     * Get rates saved in local file (converting ones from "local" to "entity" format).
     * Return empty map if local rates have not been saved yet
     */
    fun loadRates(): RatesDataEntity {
        val jsonRates = if (file.isFile) file.readText(Charsets.UTF_8) else ""
        return try {
            localToEntityMapper.map(Json.decodeFromString(jsonRates))
        } catch (ex: SerializationException) {
            Log.w(
                "RatesLocalDataSource",
                "File with local rates ('${file.name}') parsing error => ignore it.."
            )
            localToEntityMapper.map(RatesLocal(mapOf()))
        }
    }

    fun saveRates(ratesEntity: RatesDataEntity): Boolean {
        return  if (file.isFile) {
            val jsonRates = Json.encodeToString(entityToLocalMapper.map(ratesEntity))
            file.writeText(jsonRates)
            true
        } else {
            Log.w(
                "RatesLocalDataSource",
                "Saving local rates to file ('${file.name}') error => ignore it.."
            )
            false
        }
    }
}
