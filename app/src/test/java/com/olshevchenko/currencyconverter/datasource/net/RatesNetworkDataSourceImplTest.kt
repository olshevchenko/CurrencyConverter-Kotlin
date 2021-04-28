package com.olshevchenko.currencyconverter.datasource.net

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.datasource.net.api.GenericNetworkException
import com.olshevchenko.currencyconverter.datasource.net.api.MockInterceptor
import com.olshevchenko.currencyconverter.datasource.net.api.NetworkConnectionException
import com.olshevchenko.currencyconverter.datasource.net.api.RatesApiService
import com.olshevchenko.currencyconverter.datasource.net.model.ApiToEntityMapper
import com.olshevchenko.currencyconverter.datasource.net.model.RatesApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.core.classloader.annotations.PrepareForTest
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.UnknownHostException

@RunWith(MockitoJUnitRunner::class)
@PrepareForTest(
    RatesApiService::class,
    ApiToEntityMapper::class,
)

class RatesNetworkDataSourceImplTest {

    private lateinit var ratesNetworkDataSourceImpl: RatesNetworkDataSourceImpl

    @Mock
    private lateinit var apiToEntityMapperMocked: ApiToEntityMapper

    @Mock
    private lateinit var ratesApiServiceMocked: RatesApiService

    @Mock
    private lateinit var ratesApiServiceErrMocked: RatesApiService

    @Mock
    private lateinit var ratesApiServiceExceptionMocked: RatesApiService


    private lateinit var ratesErrApi: RatesApi
    private lateinit var ratesUSDEURApi: RatesApi

    private lateinit var rateUSDEURQuote: RatesDataEntity.Quote

    private lateinit var ratesEmptyEntity: RatesDataEntity
    private lateinit var ratesUSDEUREntity: RatesDataEntity

    private val observer = TestObserver<Result<RatesDataEntity>>()

    private lateinit var expectedErrResult: Result<RatesDataEntity>
    private lateinit var expectedExceptionResult: Result<RatesDataEntity>

    private lateinit var moshi: Moshi


    @Before
    fun setUp() {

        ratesErrApi = RatesApi(
            false,
            RatesApi.ErrorDesc(105, "Access Restricted."),
            null,
            null,
            null
        )
        ratesUSDEURApi = RatesApi(
            true,
            null,
            12345L,
            "USD",
            mapOf("USDEUR" to 0.8)
        )

        rateUSDEURQuote = RatesDataEntity.Quote(12345L, 0.8)

        ratesEmptyEntity = RatesDataEntity(mapOf())
        ratesUSDEUREntity = RatesDataEntity(sortedMapOf(Pair("USDEUR", rateUSDEURQuote)))

        expectedErrResult = Result.error(
            GenericNetworkException(), errorDesc = "code: 105 (Access Restricted.)"
        )

        expectedExceptionResult = Result.error(
            NetworkConnectionException(), errorDesc = "Network error."
        )

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        `when`(ratesApiServiceMocked.getRates())
            .thenReturn(Single.just(ratesUSDEURApi))

        `when`(ratesApiServiceErrMocked.getRates())
            .thenReturn(Single.just(ratesErrApi))

        `when`(apiToEntityMapperMocked.map(ratesUSDEURApi))
            .thenReturn(ratesUSDEUREntity)


    }

    @After
    fun tearDown() {
    }

    /**
     * extension fun for error Result equality check
     * intentionally ignore error.stackTrace check (always not equal!)
     */
    private fun Result<RatesDataEntity>.errEquals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is Result<*>) return false
        if ((other.resultType != Result.ResultType.ERROR) ||
            (this.resultType != Result.ResultType.ERROR)
        ) return false
//        if ((other.error == null) ||
//            (this.error == null)) return false

        val isErrorEquals = other.error?.let { othererror ->
            error?.let {
                (othererror.message == it.message) &&
                        (othererror.cause == it.cause)
            } ?: false
        } ?: false

        val isErrorDescEquals = other.errorDesc?.let { othererrordesc ->
            errorDesc?.let {
                othererrordesc == it
            } ?: false
        } ?: false

        return isErrorEquals && isErrorDescEquals
    }

    @Test
//    fun verifyGetSuccessfulRates() {
    fun `should correctly get Rates from mocked network`() {

        ratesNetworkDataSourceImpl = RatesNetworkDataSourceImpl(
            ratesApiServiceMocked, apiToEntityMapperMocked
        )
        ratesNetworkDataSourceImpl.getRates().subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(Result.success(ratesUSDEUREntity))
        verify(ratesApiServiceMocked).getRates()
        verify(apiToEntityMapperMocked).map(ratesUSDEURApi)
    }

    @Test
//    fun verifyGetErrorRates() {
    fun `should correctly proceed network fail and return error`() {

        ratesNetworkDataSourceImpl = RatesNetworkDataSourceImpl(
            ratesApiServiceErrMocked, apiToEntityMapperMocked
        )

        ratesNetworkDataSourceImpl.getRates().subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        val actualResult: Result<RatesDataEntity> = observer.values()[0]
        assertTrue(actualResult.errEquals(expectedErrResult))
        verify(ratesApiServiceErrMocked).getRates()
        verifyNoInteractions(apiToEntityMapperMocked)
    }

    @Test
//    fun verifyGetExceptionRates() {
    fun `should correctly proceed network exception and return error`() {

//       `when`(ratesApiServiceExceptionMocked.getRates())
//            .thenThrow(
//                UnknownHostException("http://mockedapilayer.net/api")
//            )
        given(ratesApiServiceExceptionMocked.getRates()).willAnswer {
            UnknownHostException("http://mockedapilayer.net/api")
        }

        ratesNetworkDataSourceImpl = RatesNetworkDataSourceImpl(
            ratesApiServiceExceptionMocked, apiToEntityMapperMocked
        )

        ratesNetworkDataSourceImpl.getRates().subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        val actualResult: Result<RatesDataEntity> = observer.values()[0]
        assertTrue(actualResult.errEquals(expectedExceptionResult))
        verify(ratesApiServiceExceptionMocked).getRates()
        verifyNoInteractions(apiToEntityMapperMocked)
    }


    @Test
//    fun verifyMockedInterceptorGetFailRates() {
    fun `should correctly proceed network fail from API with mocked interceptor`() {

        val ratesMockFailApiService = Retrofit.Builder()
            .client(
                OkHttpClient.Builder().addInterceptor(
                    MockInterceptor(MockInterceptor.RatesResponses.FAIL)
                ).build()
            )
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://apilayer.net/api/")
            .build()
            .create(RatesApiService::class.java)

        ratesNetworkDataSourceImpl = RatesNetworkDataSourceImpl(
            ratesMockFailApiService, ApiToEntityMapper
        )

        ratesNetworkDataSourceImpl.getRates().subscribe(observer)
        observer.assertNoErrors()
//        observer.assertComplete()
        val actualResult: Result<RatesDataEntity> = observer.values()[0]
        assertTrue(actualResult.errEquals(expectedErrResult))
    }

    @Test
//    fun verifyMockedInterceptorGetSuccessfulRates() {
    fun `should correctly get Rates from API with mocked interceptor`() {

        val ratesMockShortApiService = Retrofit.Builder()
            .client(
                OkHttpClient.Builder().addInterceptor(
                    MockInterceptor(MockInterceptor.RatesResponses.SHORT)
                ).build()
            )
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("http://apilayer.net/api/")
            .build()
            .create(RatesApiService::class.java)

        ratesNetworkDataSourceImpl = RatesNetworkDataSourceImpl(
            ratesMockShortApiService, ApiToEntityMapper
        )

        ratesNetworkDataSourceImpl.getRates().subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
//        var actualResult: Result<RatesDataEntity> = observer.values()[0]
        observer.assertValue(Result.success(ratesUSDEUREntity))
    }

}