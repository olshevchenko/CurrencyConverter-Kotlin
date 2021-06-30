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
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyCodes
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.converter.domain.model.FromToCodes
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Test
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
    private lateinit var ratesNetworkDataSourceImplMocked: RatesNetworkDataSourceImpl
    @Mock
    private lateinit var ratesNetworkDataSourceFailImplMocked: RatesNetworkDataSourceImpl
    @Mock
    private lateinit var ratesNetworkDataSourceInitImplMocked: RatesNetworkDataSourceImpl


    @Mock
    private lateinit var ratesCacheDataSourceImplMocked: RatesCacheDataSourceImpl
    @Mock
    private lateinit var ratesLocalDataSourceImplMocked: RatesLocalDataSourceImpl
    @Mock
    private lateinit var ratesLocalDataSourceInitImplMocked: RatesLocalDataSourceImpl
    @Mock
    private lateinit var ratesLocalDataSourceEmptyImplMocked: RatesLocalDataSourceImpl
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

    val currencyRatesEmpty = CurrencyRates(
        listOf()
    )

    val currencyRatesUSDGBP = CurrencyRates(
        listOf(currencyRateUSDGBP)
    )

    val currencyRatesUSDGBPUSDEUR = CurrencyRates(
        listOf(
            currencyRateUSDGBP,
            currencyRateUSDEUR,
        )
    )

    val currencyEmptyCodes = CurrencyCodes(listOf())
    val currencyUSDEURCodes = CurrencyCodes(listOf("USD", "EUR"))

    private val rateUSDEURQuote = RatesDataEntity.Quote(67890L, 0.8)
    private val rateUSDGBPQuote = RatesDataEntity.Quote(12345L, 0.7)

    private val ratesEmptyEntity = RatesDataEntity(mapOf())
    private lateinit var ratesUSDEUREntity: RatesDataEntity
    private lateinit var ratesUSDGBPEntity: RatesDataEntity
    private lateinit var ratesUSDGBPUSDEUREntity: RatesDataEntity

    @Before
    fun setUp() {

        ratesUSDEUREntity = RatesDataEntity(mapOf(Pair("USDEUR", rateUSDEURQuote)))
        ratesUSDGBPEntity = RatesDataEntity(mapOf(Pair("USDGBP", rateUSDGBPQuote)))
        ratesUSDGBPUSDEUREntity = RatesDataEntity(
            sortedMapOf(
                Pair("USDGBP", rateUSDGBPQuote),
                Pair("USDEUR", rateUSDEURQuote),
            )
        )

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

        `when`(entityToCurrencyRatesMapperMocked.map(ratesEmptyEntity))
            .thenReturn(currencyRatesEmpty)

        `when`(entityToCurrencyRatesMapperMocked.map(ratesUSDGBPUSDEUREntity))
            .thenReturn(currencyRatesUSDGBPUSDEUR)

        `when`(entityToCurrencyRatesMapperMocked.map(ratesUSDGBPEntity))
            .thenReturn(currencyRatesUSDGBP)

        /**
         * Mock RatesRepositoryImpl() init {} block
         */
        `when`(ratesLocalDataSourceEmptyImplMocked.loadRates())
            .thenReturn(ratesEmptyEntity)

        `when`(ratesLocalDataSourceInitImplMocked.loadRates())
            .thenReturn(ratesUSDGBPEntity)

        `when`(ratesCacheDataSourceInitImplMocked.getRates())
            .thenReturn(ratesUSDGBPEntity)

        `when`(ratesNetworkDataSourceFailImplMocked.getRates())
            .thenReturn(
                Single.just(Result.error<RatesDataEntity>(errorDesc = "network err"))
            )

    }

    @After
    fun tearDown() {
    }


    @Test
//    fun verifyInitialStepsAndGettingEmptyList() {
    fun `should execute initial steps correctly and process getRates with empty list result correctly`() {

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceFailImplMocked,
            RatesCacheDataSourceImpl(
                LocalToEntityMapper, EntityToLocalMapper, QuoteToCurrencyRateMapper,
            ),
            ratesLocalDataSourceEmptyImplMocked,
            entityToCurrencyRatesMapperMocked
        )

        val observer = TestObserver<Result<CurrencyRates>>()

        ratesRepositoryImpl.getRates().subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(Result.errorWData(
            currencyRatesEmpty, errorDesc = "Existed currency rates list is empty"))
        verify(ratesLocalDataSourceEmptyImplMocked, times(1)).loadRates()
        verifyNoInteractions(ratesNetworkDataSourceFailImplMocked)
        verify(
            entityToCurrencyRatesMapperMocked,
            times(1)
        ).map(ratesEmptyEntity)
    }

    @Test
//    fun verifyInitialStepsAndGettingCorrectList() {
    fun `should execute initial steps correctly and process getRates with correct non-empty LOCAL list result correctly`() {

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceFailImplMocked,
            ratesCacheDataSourceInitImplMocked,
            ratesLocalDataSourceInitImplMocked,
            entityToCurrencyRatesMapperMocked
        )

        val observer = TestObserver<Result<CurrencyRates>>()

        ratesRepositoryImpl.getRates().subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(Result.success(currencyRatesUSDGBP))
        verify(ratesLocalDataSourceInitImplMocked, times(1)).loadRates()
        verify(ratesCacheDataSourceInitImplMocked, times(1)).saveRates(Mockito.any())
        verify(ratesCacheDataSourceInitImplMocked, times(1)).getRates()
        verifyNoInteractions(ratesNetworkDataSourceFailImplMocked)
        verify(
            entityToCurrencyRatesMapperMocked,
            times(1)
        ).map(ratesUSDGBPEntity)
    }

    @Test
//    fun verifyRefreshWithNetworkingFail() {
    fun `should process refreshRates with network fail correctly`() {

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceFailImplMocked,
            ratesCacheDataSourceInitImplMocked,
            ratesLocalDataSourceInitImplMocked,
            entityToCurrencyRatesMapperMocked
        )

        `when`(ratesCacheDataSourceInitImplMocked.getRatesTimestamp())
            .thenReturn(357L)

        val observer = TestObserver<Result<Long>>()

        ratesRepositoryImpl.refreshRates().subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(Result.errorWData(357L, errorDesc = "network err"))
        verify(ratesCacheDataSourceInitImplMocked, times(1)).saveRates(Mockito.any())
        verify(ratesCacheDataSourceInitImplMocked, times(1)).getRates()
        verify(ratesNetworkDataSourceFailImplMocked, times(1)).getRates()
        verifyNoInteractions(entityToCurrencyRatesMapperMocked)
    }

    @Test
//    fun verifyCorrectNetworking() {
    fun `should process refreshRates with network updates correctly, then get and save rates`() {

        /**
         * mock network response to correct form
         */
        `when`(ratesNetworkDataSourceImplMocked.getRates())
            .thenReturn(
                Single.just(Result.success(ratesUSDEUREntity))
            )

        `when`(ratesLocalDataSourceInitImplMocked.saveRates(ratesUSDGBPUSDEUREntity))
            .thenReturn(true)

        /**
         * initially fill cache from mocked LocalDataSource (by ratesUSDGBPEntity)
         */
        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceImplMocked,
            RatesCacheDataSourceImpl(
                LocalToEntityMapper, EntityToLocalMapper, QuoteToCurrencyRateMapper,
            ),
            ratesLocalDataSourceInitImplMocked,
            entityToCurrencyRatesMapperMocked
        )

        val observerRefreshing = TestObserver<Result<Long>>()

        /**
         * merge initial cache value with netw. response (ratesUSDEUREntity)
         */
        ratesRepositoryImpl.refreshRates().subscribe(observerRefreshing)
        observerRefreshing.assertNoErrors()
        observerRefreshing.assertComplete()
        observerRefreshing.assertValue(Result.success(67890L))

        /**
         * get merged cache value
         */
        val observerGetting = TestObserver<Result<CurrencyRates>>()

        ratesRepositoryImpl.getRates().subscribe(observerGetting)
        observerGetting.assertNoErrors()
        observerGetting.assertComplete()
        observerGetting.assertValue(Result.success(currencyRatesUSDGBPUSDEUR))
        verify(
            entityToCurrencyRatesMapperMocked,
            times(1)
        ).map(ratesUSDGBPUSDEUREntity)

        /**
         * save merged cache value
         */
        val observerSaving = TestObserver<Result<Unit>>()
        ratesRepositoryImpl.saveRates().subscribe(observerSaving)
        observerSaving.assertNoErrors()
        observerSaving.assertComplete()
        observerSaving.assertValue(Result.success())
        verify(
            ratesLocalDataSourceInitImplMocked,
            times(1)
        ).saveRates(ratesUSDGBPUSDEUREntity)
    }

    @Test
//    fun verifyGetCodes() {
    fun `should return list of all existed pairs of codes`() {
        val observer = TestObserver<Result<CurrencyCodes>>()

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceFailImplMocked,
            ratesCacheDataSourceImplMocked,
            ratesLocalDataSourceImplMocked,
            entityToCurrencyRatesMapperMocked
        )

        ratesRepositoryImpl.getCodes().subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(Result.success(currencyUSDEURCodes))
        verifyNoInteractions(ratesNetworkDataSourceFailImplMocked)
        verify(ratesCacheDataSourceImplMocked, times(1)).getCodes()
        verify(ratesCacheDataSourceImplMocked, times(1)).saveRates(Mockito.any())
        verify(ratesLocalDataSourceImplMocked, times(1)).loadRates()
    }

    @Test
//    fun verifyGetCorrectRate() {
    fun `should return suitable rate for given pair of codes if one existed`() {
        val observer = TestObserver<Result<CurrencyRate>>()

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceFailImplMocked,
            ratesCacheDataSourceImplMocked,
            ratesLocalDataSourceImplMocked,
            entityToCurrencyRatesMapperMocked
        )

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

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceFailImplMocked,
            ratesCacheDataSourceImplMocked,
            ratesLocalDataSourceImplMocked,
            entityToCurrencyRatesMapperMocked
        )

        ratesRepositoryImpl.getRate(fromToCodesIncorrect)
            .subscribe(observer)
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(Result.error(errorDesc = "Rate NOT FOUND"))
    }

}