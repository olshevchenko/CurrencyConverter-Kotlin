package com.olshevchenko.currencyconverter.features.rates.presentation

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.RatesRepositoryImpl
import com.olshevchenko.currencyconverter.core.ui.ViewState
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.rates.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.rates.domain.usecase.GetCurrencyRatesUseCase
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.core.classloader.annotations.PrepareForTest


@RunWith(MockitoJUnitRunner::class)
@PrepareForTest(
    RatesListView::class,
    RatesRepositoryImpl::class,
)

class RatesListPresenterTest {

    //    @Suppress("UNCHECKED_CAST")
//    private fun <T> any(type: Class<T>): T {
//        Mockito.any<T>(type)
//        return null as T
//    }

    @Mock
    private lateinit var ratesRepositoryImplMocked: RatesRepositoryImpl

    @Mock
    private lateinit var ratesListViewMocked: RatesListView

    private lateinit var getCurrencyRatesUseCase: GetCurrencyRatesUseCase
    private lateinit var ratesListPresenter: RatesListPresenter

    val currencyRateUSDEUR = CurrencyRate(
        "USD", "EUR",
        0.8, 12345L.toString()
    )
    val currencyRatesUSDEUR = CurrencyRates(
        listOf(currencyRateUSDEUR)
    )
    val goodResult = Result.success(currencyRatesUSDEUR)

    @Before
    fun setUp() {

        `when`(ratesRepositoryImplMocked.getRates())
            .thenReturn(
                Single.just(goodResult)
            )

//        val valueCapture = ArgumentCaptor.forClass(
//            ViewState::class.java
//        )
//
//        doNothing().`when`(
//            ratesListViewMocked.setViewState(valueCapture.capture() as RatesViewState)
//        )

        doNothing().`when`(ratesListViewMocked).setViewState(RatesViewState.loading())
        doNothing().`when`(ratesListViewMocked).setViewState(RatesViewState.loadingFinished())

        getCurrencyRatesUseCase = GetCurrencyRatesUseCase(
            ratesRepositoryImplMocked,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )

        ratesListPresenter = RatesListPresenter(getCurrencyRatesUseCase)
    }

    @After
    fun tearDown() {
    }


    @Test
//    fun verifyNothingHappensInitially() {
    fun `shouldn't get data (currency rates) before direct initialization or request`() {
        verifyNoInteractions(ratesRepositoryImplMocked)
        verifyNoInteractions(ratesListViewMocked)
        getCurrencyRatesUseCase.dispose()
    }

    @Test
//    fun verifyCorrectInitialization() {
    fun `should correctly get currency rates for the first time during initialization`() {
        ratesListPresenter.attachView(ratesListViewMocked)
        ratesListPresenter.init()

        verify(ratesListViewMocked).initView()
        verify(ratesListViewMocked).setViewState(RatesViewState.loading())
        verify(ratesListViewMocked).setViewState(RatesViewState.loadingFinished())
        verify(ratesListViewMocked).setViewState(
            RatesViewState.success(listOf(currencyRateUSDEUR)))
        verifyNoMoreInteractions(ratesListViewMocked)

        verify(ratesRepositoryImplMocked).getRates()
        verifyNoMoreInteractions(ratesRepositoryImplMocked)
        getCurrencyRatesUseCase.dispose()
    }
}