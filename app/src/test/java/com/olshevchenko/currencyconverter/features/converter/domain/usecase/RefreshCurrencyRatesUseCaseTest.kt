package com.olshevchenko.currencyconverter.features.converter.domain.usecase

import android.util.Log
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.RatesRepositoryImpl
import com.olshevchenko.currencyconverter.core.data.entity.EntityToCurrencyRatesMapper
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRates
import io.reactivex.Single
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
    RatesRepositoryImpl::class,
    RefreshCurrencyRatesUseCaseTest.FuncHandlerTest::class,
)

class RefreshCurrencyRatesUseCaseTest {

    class FuncHandlerTest {
        fun onSubscribeFunc() {}
        fun finallyFunc() {}
    }

    @Mock
    private lateinit var ratesRepositoryImplMocked: RatesRepositoryImpl

    @Mock
    private lateinit var funcHandlerTestMocked: FuncHandlerTest

    private lateinit var refreshCurrencyRatesUseCase: RefreshCurrencyRatesUseCase


    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
//        refreshCurrencyRatesUseCase.dispose()
    }


    @Test
//    fun verifyProcessFailOfRefreshingRates() {
    fun `should correctly process fail of refreshing currency rates`() {

        `when`(ratesRepositoryImplMocked.refreshRates())
            .thenReturn(
                Single.just(
                    Result.errorWData(
                        0L,
                        errorDesc = "network err"
                    )
                )
            )

        refreshCurrencyRatesUseCase = RefreshCurrencyRatesUseCase(
            ratesRepositoryImplMocked,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )

        val failResult: Result<Long> = Result.errorWData(
            0L,
            errorDesc = "network err"
        )
        val observer = TestObserver<Result<Long>>()
        refreshCurrencyRatesUseCase.execute(
            observer,
            {
                funcHandlerTestMocked.onSubscribeFunc()
            },
            {
                Log.i(
                    "RefreshCurrencyRatesUseCaseTest",
                    "AfterTerminateFuncHandler is calling..."
                )
            }

        )
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(failResult)
        verify(funcHandlerTestMocked).onSubscribeFunc()
        verifyNoMoreInteractions(funcHandlerTestMocked)

        refreshCurrencyRatesUseCase.dispose()
    }


    @Test
//    fun verifyGetCorrectRefreshRatesResult() {
    fun `should correctly get successful refresh result`() {

        `when`(ratesRepositoryImplMocked.refreshRates())
            .thenReturn(
                Single.just(Result.success(12345L))
            )

        refreshCurrencyRatesUseCase = RefreshCurrencyRatesUseCase(
            ratesRepositoryImplMocked,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )

        val goodResult: Result<Long> = Result.success(
            12345L
        )
        val observer = TestObserver<Result<Long>>()
        refreshCurrencyRatesUseCase.execute(
            observer,
            {
                Log.i(
                    "RefreshCurrencyRatesUseCaseTest",
                    "onSubscribeFuncHandler is calling..."
                )
            },
            {
                funcHandlerTestMocked.finallyFunc()
            }
        )
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(goodResult)
        verify(funcHandlerTestMocked).finallyFunc()
        verifyNoMoreInteractions(funcHandlerTestMocked)
        verify(ratesRepositoryImplMocked).refreshRates()
        verifyNoMoreInteractions(ratesRepositoryImplMocked)

        refreshCurrencyRatesUseCase.dispose()
    }

}