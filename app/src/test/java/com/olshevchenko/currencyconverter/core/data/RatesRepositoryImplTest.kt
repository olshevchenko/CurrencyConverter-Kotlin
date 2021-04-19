package com.olshevchenko.currencyconverter.core.data

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.entity.EntityToCurrencyRatesMapper
import com.olshevchenko.currencyconverter.core.data.entity.QuoteToCurrencyRateMapper
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.datasource.cache.RatesCacheDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.local.RatesLocalDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.local.model.EntityToLocalMapper
import com.olshevchenko.currencyconverter.datasource.local.model.LocalToEntityMapper
import com.olshevchenko.currencyconverter.datasource.net.RatesNetworkDataSourceImpl
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.rates.domain.model.FromToCodes
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.core.classloader.annotations.PrepareForTest
import java.time.LocalDateTime

@RunWith(MockitoJUnitRunner::class)
@PrepareForTest(
    RatesNetworkDataSourceImpl::class,
    RatesLocalDataSourceImpl::class,
    RatesCacheDataSourceImpl::class,
    EntityToCurrencyRatesMapper::class,
)

class RatesRepositoryImplTest {

    private lateinit var ratesRepositoryImpl: RatesRepositoryImpl

    @Mock
    private lateinit var ratesNetworkDataSourceInitImplMocked: RatesNetworkDataSourceImpl

    @Mock
    private lateinit var ratesCacheDataSourceImplMocked: RatesCacheDataSourceImpl

    @Mock
    private lateinit var ratesLocalDataSourceImplMocked: RatesLocalDataSourceImpl

    @Mock
    private lateinit var ratesLocalDataSourceInitImplMocked: RatesLocalDataSourceImpl

    @Mock
    private lateinit var ratesCacheDataSourceInitImplMocked: RatesCacheDataSourceImpl

    @Mock
    private lateinit var entityToCurrencyRatesMapperMocked: EntityToCurrencyRatesMapper

    val fromToCodesCorrect = FromToCodes("USD", "USD")
    val fromToCodesIncorrect = FromToCodes("EUR", "EUR")

    val currencyRateUSDUSD = CurrencyRate(
        "USD", "USD",
        1.0, LocalDateTime.now().toString()
    )
    val currencyRateUSDGBP = CurrencyRate(
        "USD", "GBP",
        0.7, 12345L.toString()
    )
    val currencyRateUSDEUR = CurrencyRate(
        "USD", "EUR",
        0.8, 12345L.toString()
    )

    val currencyRatesUSDGBP = CurrencyRates(
        listOf(currencyRateUSDGBP))

    val currencyRatesUSDGBPUSDEUR = CurrencyRates(
        listOf(
            currencyRateUSDGBP,
            currencyRateUSDEUR,
        ))

    val currencyEmptyCodes = CurrencyCodes(listOf())
    val currencyUSDEURCodes = CurrencyCodes(listOf("USD", "EUR"))

    private val rateUSDEURQuote = RatesDataEntity.Quote(12345L, 0.8)
    private val rateUSDGBPQuote = RatesDataEntity.Quote(12345L, 0.7)

    private val ratesEmptyEntity = RatesDataEntity(mapOf())
    private lateinit var ratesUSDEUREntity: RatesDataEntity
    private lateinit var ratesUSDGBPEntity: RatesDataEntity
    private lateinit var ratesUSDGBPUSDEUREntity: RatesDataEntity

    @Before
    fun setUp() {

        ratesUSDEUREntity = RatesDataEntity(mapOf(Pair("USDEUR", rateUSDEURQuote)))
        ratesUSDGBPEntity = RatesDataEntity(mapOf(Pair("USDGBP", rateUSDGBPQuote)))
        ratesUSDGBPUSDEUREntity = RatesDataEntity(sortedMapOf(
            Pair("USDGBP", rateUSDGBPQuote),
            Pair("USDEUR", rateUSDEURQuote),
        ))

//        `when`(ratesLocalDataSourceImplMocked.saveRates(ratesUSDEUREntity))
//            .thenReturn(true)

        `when`(ratesLocalDataSourceImplMocked.loadRates())
            .thenReturn(ratesUSDEUREntity)


        `when`(ratesCacheDataSourceImplMocked.getCodes())
            .thenReturn(currencyUSDEURCodes)

        `when`(ratesCacheDataSourceImplMocked.getRate(fromToCodesCorrect))
            .thenReturn(currencyRateUSDUSD)

        `when`(ratesCacheDataSourceImplMocked.getRate(fromToCodesIncorrect))
            .thenReturn(null)

        `when`(entityToCurrencyRatesMapperMocked.map(ratesUSDGBPUSDEUREntity))
            .thenReturn(currencyRatesUSDGBPUSDEUR)

        `when`(entityToCurrencyRatesMapperMocked.map(ratesUSDGBPEntity))
            .thenReturn(currencyRatesUSDGBP)

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceInitImplMocked,
            ratesCacheDataSourceImplMocked,
            ratesLocalDataSourceImplMocked,
            entityToCurrencyRatesMapperMocked
        )

        /**
         * Mock RatesRepositoryImpl() init {} block
         */
        `when`(ratesLocalDataSourceInitImplMocked.loadRates())
            .thenReturn(ratesUSDGBPEntity)

        `when`(ratesCacheDataSourceInitImplMocked.getRates())
            .thenReturn(ratesUSDGBPEntity)

        `when`(ratesNetworkDataSourceInitImplMocked.getRates())
            .thenReturn(
                Single.just(Result.error<RatesDataEntity>(errorDesc = "network err"))
            )


    }

    @After
    fun tearDown() {
    }


    @Test
//    fun verifyInitialStepsAndNetworking1() {
    fun `should execute initial steps correctly and process network fail correctly`() {

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceInitImplMocked,
            ratesCacheDataSourceInitImplMocked,
            ratesLocalDataSourceInitImplMocked,
            entityToCurrencyRatesMapperMocked
        )

        val observer = TestObserver<Result<CurrencyRates>>()

        ratesRepositoryImpl.getRates().subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(Result.errorWData(currencyRatesUSDGBP, errorDesc = "network err"))
        Mockito.verify(ratesLocalDataSourceInitImplMocked, Mockito.times(1)).loadRates()
        Mockito.verify(ratesCacheDataSourceInitImplMocked, Mockito.times(1)).saveRates(Mockito.any())
        Mockito.verify(ratesCacheDataSourceInitImplMocked, Mockito.times(1)).getRates()
        Mockito.verify(ratesNetworkDataSourceInitImplMocked, Mockito.times(1)).getRates()
        verify(entityToCurrencyRatesMapperMocked,
            Mockito.times(1)).map(ratesUSDGBPEntity)
    }

    @Test
//    fun verifyInitialStepsAndNetworking2() {
    fun `should execute initial steps correctly and merging with network updates correctly, then save rates`() {

        /**
         * mock network response to correct form
         */
        `when`(ratesNetworkDataSourceInitImplMocked.getRates())
            .thenReturn(
                Single.just(Result.success(ratesUSDEUREntity))
            )

        `when`(ratesLocalDataSourceInitImplMocked.saveRates(ratesUSDGBPUSDEUREntity))
            .thenReturn(true)

        /**
         * initially fill cache from mocked LocalDataSource (by ratesUSDGBPEntity)
         */
        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceInitImplMocked,
            RatesCacheDataSourceImpl(
                LocalToEntityMapper, EntityToLocalMapper, QuoteToCurrencyRateMapper,
            ),
            ratesLocalDataSourceInitImplMocked,
            entityToCurrencyRatesMapperMocked
        )

        val observerGetting = TestObserver<Result<CurrencyRates>>()

        /**
         * merge initial cache value with netw. response (ratesUSDEUREntity)
         */
        ratesRepositoryImpl.getRates().subscribe(observerGetting)
        observerGetting.assertNoErrors()
        observerGetting.assertComplete()
        observerGetting.assertValue(Result.success(currencyRatesUSDGBPUSDEUR))
        verify(entityToCurrencyRatesMapperMocked,
            Mockito.times(1)).map(ratesUSDGBPUSDEUREntity)

        /**
         * save merged cache value
         */
        val observerSaving = TestObserver<Result<Unit>>()
        ratesRepositoryImpl.saveRates().subscribe(observerSaving)
        observerSaving.assertNoErrors()
        observerSaving.assertComplete()
        observerSaving.assertValue(Result.success(Unit))
        verify(ratesLocalDataSourceInitImplMocked,
            Mockito.times(1)).saveRates(ratesUSDGBPUSDEUREntity)
    }

    @Test
//    fun verifyGetCodes() {
    fun `should return list of all existed pairs of codes`() {
        val observer = TestObserver<Result<CurrencyCodes>>()

        ratesRepositoryImpl.getCodes().subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(Result.success(currencyUSDEURCodes))
        Mockito.verifyNoInteractions(ratesNetworkDataSourceInitImplMocked)
        Mockito.verify(ratesCacheDataSourceImplMocked, Mockito.times(1)).getCodes()
        Mockito.verify(ratesCacheDataSourceImplMocked, Mockito.times(1)).saveRates(Mockito.any())
        Mockito.verify(ratesLocalDataSourceImplMocked, Mockito.times(1)).loadRates()
    }

    @Test
//    fun verifyGetCorrectRate() {
    fun `should return suitable rate for given pair of codes if one existed`() {
        val observer = TestObserver<Result<CurrencyRate>>()

        ratesRepositoryImpl.getRate(fromToCodesCorrect).subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
//        observer.assertValueAt(0, Result.success(currencyRate))
        observer.assertValue(Result.success(currencyRateUSDUSD))
    }

    @Test
//    fun verifyGetIncorrectRate() {
    fun `should return error for unknown pair of codes`() {
        val observer = TestObserver<Result<CurrencyRate>>()

        ratesRepositoryImpl.getRate(fromToCodesIncorrect)
            .subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(Result.error(errorDesc = "Rate NOT FOUND"))
    }

}