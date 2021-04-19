package com.olshevchenko.currencyconverter.datasource.net.api

import com.olshevchenko.currencyconverter.datasource.net.model.RatesApi
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

private const val ACCESS_KEY = "e2c9410aabead14100bc12068ecc394b"


interface RatesApiService {
    @GET("live")
    fun getRates(
        @Query("access_key") accessKey: String = ACCESS_KEY,
        @Query("format") formatValue: Int = 1,
    ): Single<RatesApi>
}
