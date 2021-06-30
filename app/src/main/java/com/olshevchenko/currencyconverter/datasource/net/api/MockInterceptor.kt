package com.olshevchenko.currencyconverter.datasource.net.api

import android.util.Log
import com.olshevchenko.currencyconverter.BuildConfig
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody

class MockInterceptor(var responseType: RatesResponses) : Interceptor {

    enum class RatesResponses(var json: String) {
        FAIL("""
        {
            "success":false,
            "error":{
                "code":105,
                "info":"Access Restricted."
            }
        }
        """
        ),
        SHORT("""
        {
            "success":true,
            "timestamp":12345,
            "source":"USD",
            "quotes":{
                "USDEUR":0.8
            }
        }    
        """
        ),
        LONG("""
        {
            "success":true,
            "timestamp":12345,
            "source":"USD",
            "quotes":{
                "USDEUR":0.8
                "USDGBP":0.7,
                "USDRUB":77.0
            }
        }    
        """
        )
    }


    override fun intercept(chain: Interceptor.Chain): Response {
        if (!BuildConfig.DEBUG) {
            throw IllegalAccessError(
                "MockInterceptor is for Test/Debug purposes only and to be used with DEBUG mode"
            )
        }
//        val uri = chain.request().url().uri().toString()
//        Log.i(
//            "MockInterceptor",
//            "uri = '$uri'"
//        )

        return chain.proceed(chain.request())
            .newBuilder()
            .code(200)
            .protocol(Protocol.HTTP_2)
            .message(responseType.json)
            .body(
                responseType.json.toByteArray()
                    .toResponseBody("application/json".toMediaTypeOrNull())
            )
            .addHeader("content-type", "application/json")
            .build()
    }
}
