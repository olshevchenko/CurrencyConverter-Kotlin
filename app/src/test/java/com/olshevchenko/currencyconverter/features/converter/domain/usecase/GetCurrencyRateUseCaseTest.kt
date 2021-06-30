package com.olshevchenko.currencyconverter.features.converter.domain.usecase

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.RatesRepositoryImpl
import com.olshevchenko.currencyconverter.core.data.entity.EntityToCurrencyRatesMapper
import com.olshevchenko.currencyconverter.core.data.entity.QuoteToCurrencyRateMapper
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.datasource.cache.RatesCacheDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.local.RatesLocalDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.local.model.EntityToLocalMapper
import com.olshevchenko.currencyconverter.datasource.local.model.LocalToEntityMapper
import com.olshevchenko.currencyconverter.datasource.net.RatesNetworkDataSourceImpl
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.converter.domain.model.FromToCodes
import io.reactivex.observers.TestObserver
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.core.classloader.annotations.PrepareForTest
import java.io.File

@RunWith(MockitoJUnitRunner::class)
@PrepareForTest(
    RatesNetworkDataSourceImpl::class,
//    RatesCacheDataSourceImpl::class,
    RatesLocalDataSourceImpl::class,
)

class GetCurrencyRateUseCaseTest {

    private lateinit var getCurrencyRateUseCase: GetCurrencyRateUseCase

    private val fromToCodesNulled: FromToCodes? = null

    private val fromToCodesWrong = FromToCodes("EUR", "EUR")

    private val fromToCodesUSDUSD = FromToCodes("USD", "USD")

    private val rateNotFoundResult: Result<CurrencyRate> =
        Result.error(errorDesc = "Rate NOT FOUND")

    private val rateUSDUSDResult: Result<CurrencyRate> =
        Result.success(
            CurrencyRate(
                "USD", "USD",
                1.0, 12345L.toString()
            )
        )

    @Mock
    private lateinit var ratesNetworkDataSourceImplMocked: RatesNetworkDataSourceImpl

    private val ratesCacheDataSourceImpl = RatesCacheDataSourceImpl(
        LocalToEntityMapper, EntityToLocalMapper, QuoteToCurrencyRateMapper
    )

    @Mock
    private lateinit var ratesLocalDataSourceImplMocked: RatesLocalDataSourceImpl

    private val ratesLocalDataSourceImpl = RatesLocalDataSourceImpl(
        File(""), LocalToEntityMapper, EntityToLocalMapper
    )


//    @Mock
//    private lateinit var ratesCacheDataSourceImplMocked: RatesCacheDataSourceImpl
//
//    @Mock
//    private lateinit var ratesLocalDataSourceImplMocked: RatesLocalDataSourceImpl

    private lateinit var ratesRepositoryImpl: RatesRepositoryImpl

    lateinit var rateUSDUSDQuote: RatesDataEntity.Quote

    lateinit var ratesUSDUSDEntity: RatesDataEntity
    val ratesEmptyEntity = RatesDataEntity(mapOf())


    @Before
    fun setUp() {

        rateUSDUSDQuote = RatesDataEntity.Quote(12345L, 1.0)
        ratesUSDUSDEntity = RatesDataEntity(sortedMapOf(Pair("USDUSD", rateUSDUSDQuote)))

        `when`(ratesLocalDataSourceImplMocked.loadRates())
            .thenReturn(ratesUSDUSDEntity)
//            .thenReturn(ratesEmptyEntity)

//        `when`(ratesNetworkDataSourceImplMocked.getRates())
//            .thenReturn(
//                Single.just(Result.success(ratesUSDUSDEntity))
//            )
//
//        `when`(ratesCacheDataSourceImplMocked.getRate(fromToCodesNulled))
//            .thenReturn(null)

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceImplMocked,
            ratesCacheDataSourceImpl, // instance of REAL CacheDataSourceImpl that got necessary
            // "USD-USD rates from mocked LocalDataSourceImpl object
            ratesLocalDataSourceImplMocked,
//            ratesLocalDataSourceImpl
            EntityToCurrencyRatesMapper
        )

        getCurrencyRateUseCase = GetCurrencyRateUseCase(
            ratesRepositoryImpl,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )
    }

    @After
    fun tearDown() {
        getCurrencyRateUseCase.dispose()
    }

    @Test
//    fun verifyGetRate4NulledCodes() {
    fun `should get 'NOT FOUND' result for nulled 'from-to' currency code`() {
        val observer = TestObserver<Result<CurrencyRate>>()

        getCurrencyRateUseCase.execute(observer, {}, {}, fromToCodesNulled)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(rateNotFoundResult)
        verifyNoInteractions(ratesNetworkDataSourceImplMocked)
//        verify(ratesCacheDataSourceImplMocked).getRate(fromToCodesNulled)
        verify(ratesLocalDataSourceImplMocked, times(1)).loadRates()
    }

    @Test
//    fun verifyGetRate4WrongCodes() {
    fun `should get 'NOT FOUND' result for incorrect 'from-to' currency code`() {
        val observer = TestObserver<Result<CurrencyRate>>()

        getCurrencyRateUseCase.execute(observer, {}, {}, fromToCodesWrong)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(rateNotFoundResult)
        verifyNoInteractions(ratesNetworkDataSourceImplMocked)
//        verify(ratesCacheDataSourceImplMocked).getRate(fromToCodesNulled)
        verify(ratesLocalDataSourceImplMocked, times(1)).loadRates()
    }

    @Test
//    fun verifyGetRate4USDUSDCodes() {
    fun `should get correct result for correct and suitable 'from-to' currency code`() {
        val observer = TestObserver<Result<CurrencyRate>>()

        getCurrencyRateUseCase.execute(observer, {}, {}, fromToCodesUSDUSD)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(rateUSDUSDResult)
        verifyNoInteractions(ratesNetworkDataSourceImplMocked)
//        verify(ratesCacheDataSourceImplMocked).getRate(fromToCodesNulled)
        verify(ratesLocalDataSourceImplMocked, times(1)).loadRates()
    }

}