package com.olshevchenko.currencyconverter.features.converter.presentation

import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.RatesRepositoryImpl
import com.olshevchenko.currencyconverter.core.presentation.RefreshViewState
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRates
import com.olshevchenko.currencyconverter.features.converter.domain.usecase.RefreshCurrencyRatesUseCase
import com.olshevchenko.currencyconverter.features.converter.presentation.model.RefreshTimeStampToUIMapper
import io.mockk.*
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test


class RefreshPresenterTest {

    val ratesRepositoryImplMocked = mockk<RatesRepositoryImpl>()
    val ratesRepositoryFailImplMocked = mockk<RatesRepositoryImpl>()

    val converterViewModelMocked = mockk<ConverterViewModel>()

    val refreshTimeStampToUIMapperMocked = mockk<RefreshTimeStampToUIMapper>()

    private lateinit var refreshCurrencyRatesUseCase: RefreshCurrencyRatesUseCase
    private lateinit var refreshCurrencyRatesFailUseCase: RefreshCurrencyRatesUseCase

    private lateinit var refreshPresenter: RefreshPresenter

    val currencyRateUSDEUR = CurrencyRate(
        "USD", "EUR",
        0.8, 12345L.toString()
    )
    val currencyRatesUSDEUR = CurrencyRates(
        listOf(currencyRateUSDEUR)
    )
    val goodResult = Result.success(12345L)
    val goodDate = "2021-01-01 12:34:50"

    val badResult = Result.errorWData(
        0L,
        errorDesc = "network err"
    )
    val badDate = "2021-01-01 00:00:00"

    @Before
    fun setUp() {

        every { ratesRepositoryImplMocked.refreshRates() } returns Single.just(goodResult)
        every { ratesRepositoryFailImplMocked.refreshRates() } returns Single.just(badResult)

        every { refreshTimeStampToUIMapperMocked.map(12345L) } returns goodDate
        every { refreshTimeStampToUIMapperMocked.map(0L) } returns badDate

        every { converterViewModelMocked.setRefreshViewState(any()) } returns Unit

        refreshCurrencyRatesUseCase = RefreshCurrencyRatesUseCase(
            ratesRepositoryImplMocked,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )

        refreshPresenter = RefreshPresenter(
            refreshCurrencyRatesUseCase,
            converterViewModelMocked,
            refreshTimeStampToUIMapperMocked
        )
    }

    @After
    fun tearDown() {
    }


    @Test
//    fun verifyNothingHappensInitially() {
    fun `shouldn't refresh data (currency rates) before direct initialization or request`() {
//        verifyNoInteractions(ratesRepositoryImplMocked)
        verify { ratesRepositoryImplMocked wasNot Called }
        verify { converterViewModelMocked wasNot Called }

        refreshCurrencyRatesUseCase.dispose()
    }

    @Test
//    fun verifyCorrectInitialization() {
    fun `should correctly refresh rates for the first time during initialization`() {
        refreshPresenter.init()
        verifySequence {
            converterViewModelMocked.setRefreshViewState(RefreshViewState.init())
            converterViewModelMocked.setRefreshViewState(RefreshViewState.inProgress())
            converterViewModelMocked.setRefreshViewState(RefreshViewState.success(goodDate))
            converterViewModelMocked.setRefreshViewState(RefreshViewState.ready())
        }
        confirmVerified(converterViewModelMocked)

        verify { ratesRepositoryImplMocked.refreshRates() }
        confirmVerified(ratesRepositoryImplMocked)
        refreshCurrencyRatesUseCase.dispose()
    }

    @Test
//    fun verifyProcessFailOfRefreshRatesAction() {
    fun `should correctly process fail results of refreshing action`() {

        /**
         * Recreate UseCase instance
         */
        refreshCurrencyRatesFailUseCase = RefreshCurrencyRatesUseCase(
            ratesRepositoryFailImplMocked,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )

        /**
         * ... and presenter
         */
        refreshPresenter = RefreshPresenter(
            refreshCurrencyRatesFailUseCase,
            converterViewModelMocked,
            refreshTimeStampToUIMapperMocked
        )

        refreshPresenter.onRefreshRatesAction()
        verifySequence {
            converterViewModelMocked.setRefreshViewState(RefreshViewState.inProgress())
            converterViewModelMocked.setRefreshViewState(
                RefreshViewState.error(badDate, errorDesc = "network err")
            )
            converterViewModelMocked.setRefreshViewState(RefreshViewState.ready())
        }
        confirmVerified(converterViewModelMocked)

        verify { ratesRepositoryFailImplMocked.refreshRates() }
        confirmVerified(ratesRepositoryFailImplMocked)
        refreshCurrencyRatesFailUseCase.dispose()
    }
}