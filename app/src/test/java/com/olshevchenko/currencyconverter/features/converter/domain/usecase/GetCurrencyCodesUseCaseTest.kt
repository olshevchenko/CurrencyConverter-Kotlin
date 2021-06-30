package com.olshevchenko.currencyconverter.features.converter.domain.usecase

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.RatesRepositoryImpl
import com.olshevchenko.currencyconverter.core.data.entity.EntityToCurrencyRatesMapper
import com.olshevchenko.currencyconverter.datasource.cache.RatesCacheDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.local.RatesLocalDataSourceImpl
import com.olshevchenko.currencyconverter.datasource.net.RatesNetworkDataSourceImpl
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyCodes
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

@RunWith(MockitoJUnitRunner::class)
@PrepareForTest(
    RatesNetworkDataSourceImpl::class,
    RatesCacheDataSourceImpl::class,
    RatesLocalDataSourceImpl::class,
)

class GetCurrencyCodesUseCaseTest {

    private lateinit var ratesRepositoryImpl: RatesRepositoryImpl

    private lateinit var getCurrencyCodesUseCase: GetCurrencyCodesUseCase


    private val emptyCurrencyCodes = CurrencyCodes(
        listOf()
    )
    private val emptyCodesResult: Result<CurrencyCodes> =
        Result.success(emptyCurrencyCodes)


    private val uSDCurrencyCodes = CurrencyCodes(
        listOf("USD")
    )
    private val uSDCodesResult: Result<CurrencyCodes> =
        Result.success(uSDCurrencyCodes)


    private val uSDEURRUBCurrencyCodes = CurrencyCodes(
        listOf("USD", "EUR", "RUB")
    )
    private val uSDEURRUBCodesResult: Result<CurrencyCodes> =
        Result.success(uSDEURRUBCurrencyCodes)

    @Mock
    private lateinit var ratesNetworkDataSourceImplMocked: RatesNetworkDataSourceImpl

    @Mock
    private lateinit var ratesCacheDataSourceImplMocked: RatesCacheDataSourceImpl

    @Mock
    private lateinit var ratesCacheDataSourceEmptyImplMocked: RatesCacheDataSourceImpl

    @Mock
    private lateinit var ratesLocalDataSourceImplMocked: RatesLocalDataSourceImpl


    @Before
    fun setUp() {

        `when`(ratesCacheDataSourceImplMocked.getCodes())
            .thenReturn(uSDEURRUBCurrencyCodes)

        `when`(ratesCacheDataSourceEmptyImplMocked.getCodes())
            .thenReturn(emptyCurrencyCodes)

    }

    @After
    fun tearDown() {
        getCurrencyCodesUseCase.dispose()
    }


    @Test
//    fun verifyGetCorrectCodes() {
    fun `should get correct result for non-empty cache repository`() {
        val observer = TestObserver<Result<CurrencyCodes>>()

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceImplMocked,
            ratesCacheDataSourceImplMocked,
            ratesLocalDataSourceImplMocked,
            EntityToCurrencyRatesMapper
        )

        getCurrencyCodesUseCase = GetCurrencyCodesUseCase(
            ratesRepositoryImpl,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )

        getCurrencyCodesUseCase.execute(observer, {}, {})
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(uSDEURRUBCodesResult)
        verifyNoInteractions(ratesNetworkDataSourceImplMocked)
        verify(ratesCacheDataSourceImplMocked, times(1)).getCodes()
        verify(ratesCacheDataSourceImplMocked, times(1)).saveRates(any())
        verify(ratesLocalDataSourceImplMocked, times(1)).loadRates()
    }

    @Test
//    fun verifyGetEmptyCodes() {
    fun `should get empty but still success result for empty cache repository`() {
        val observer = TestObserver<Result<CurrencyCodes>>()

        ratesRepositoryImpl = RatesRepositoryImpl(
            ratesNetworkDataSourceImplMocked,
            ratesCacheDataSourceEmptyImplMocked,
            ratesLocalDataSourceImplMocked,
            EntityToCurrencyRatesMapper
        )

        getCurrencyCodesUseCase = GetCurrencyCodesUseCase(
            ratesRepositoryImpl,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )

        getCurrencyCodesUseCase.execute(observer, {}, {})
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(emptyCodesResult)
        verifyNoInteractions(ratesNetworkDataSourceImplMocked)
        verify(ratesCacheDataSourceEmptyImplMocked, times(1)).getCodes()
        verify(ratesCacheDataSourceEmptyImplMocked, times(1)).saveRates(any())
        verify(ratesLocalDataSourceImplMocked, times(1)).loadRates()
    }

}