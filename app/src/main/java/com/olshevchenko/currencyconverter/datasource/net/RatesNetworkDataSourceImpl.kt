package com.olshevchenko.currencyconverter.datasource.net

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.datasource.net.api.RatesApiService
import com.olshevchenko.currencyconverter.datasource.net.api.BadRequestException
import com.olshevchenko.currencyconverter.datasource.net.api.handleNetworkExceptions
import com.olshevchenko.currencyconverter.datasource.net.model.ApiToEntityMapper
import com.olshevchenko.currencyconverter.datasource.net.model.RatesApi
import io.reactivex.Single
import retrofit2.HttpException
import retrofit2.Response

class RatesNetworkDataSourceImpl(
    private val ratesApiService: RatesApiService,
    private val apiToEntityMapper: ApiToEntityMapper
) {

    fun getRates(): Single<Result<RatesDataEntity>> {
        return try {
            ratesApiService.getRates()
            .map {
                if (it.success) { // success response w. quotes => convert one to dataEntity level
                    Result.success(apiToEntityMapper.map(it))
                } else { // error response from "apilayer.net" w/o quotes
                    Result.error(
//                        handleNetworkExceptions(HttpException(Response<RatesApi>it)),
                        handleNetworkExceptions(BadRequestException()),
                        "code: ${it.error!!.code} (${it.error.info})"
                    )
                }
            }
        } catch (ex: Exception) { // got some netw. mistake
            Single.just(Result.error(
                handleNetworkExceptions(ex),
                "Network error."
            ))
        }
    }
}