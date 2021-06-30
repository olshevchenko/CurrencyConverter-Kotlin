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
    GetCurrencyRatesUseCaseTest.FuncHandlerTest::class,
)

class GetCurrencyRatesUseCaseTest {

    class FuncHandlerTest {
        fun onSubscribeFunc() {}
        fun finallyFunc() {}
    }

    @Mock
    private lateinit var ratesRepositoryImplMocked: RatesRepositoryImpl

    @Mock
    private lateinit var funcHandlerTestMocked: FuncHandlerTest

    private lateinit var getCurrencyRatesUseCase: GetCurrencyRatesUseCase

    private lateinit var ratesUSDEUREntity: RatesDataEntity

    lateinit var rateUSDEURQuote: RatesDataEntity.Quote


    @Before
    fun setUp() {

        rateUSDEURQuote = RatesDataEntity.Quote(12345L, 0.8)
        ratesUSDEUREntity = RatesDataEntity(mapOf(Pair("USDEUR", rateUSDEURQuote)))

//        getCurrencyRatesUseCase = GetCurrencyRatesUseCase(
//            ratesRepositoryImplMocked,
//            Schedulers.trampoline(),
//            Schedulers.trampoline()
//        )
    }

    @After
    fun tearDown() {
//        getCurrencyRatesUseCase.dispose()
    }


    @Test
//    fun verifyProcessFailOfGettingRates() {
    fun `should correctly process fail of getting currency rates`() {

        `when`(ratesRepositoryImplMocked.getRates())
            .thenReturn(
                Single.just(
                    Result.errorWData(
                        CurrencyRates(listOf()),
                        errorDesc = "network err"
                    )
                )
            )

        getCurrencyRatesUseCase = GetCurrencyRatesUseCase(
            ratesRepositoryImplMocked,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )

        val failResult = Result.errorWData(
            CurrencyRates(listOf()),
            errorDesc = "network err"
        )

        val observer = TestObserver<Result<CurrencyRates>>()
        getCurrencyRatesUseCase.execute(
            observer,
            {
                funcHandlerTestMocked.onSubscribeFunc()
            },
            {
                Log.i(
                    "GetCurrencyRatesUseCaseTest",
                    "finallyFuncHandler is calling..."
                )
            }

        )
        observer.assertNoErrors()
        observer.assertComplete()
        observer.assertValue(failResult)
        verify(funcHandlerTestMocked).onSubscribeFunc()
        verifyNoMoreInteractions(funcHandlerTestMocked)

        getCurrencyRatesUseCase.dispose()
    }


    @Test
//    fun verifyGetCorrectRatesWithEntityToCurrencyRatesMapper() {
    fun `should correctly get successful rates mapped by EntityToCurrencyRatesMapper`() {

        `when`(ratesRepositoryImplMocked.getRates())
            .thenReturn(
                Single.just(Result.success(EntityToCurrencyRatesMapper.map(ratesUSDEUREntity)))
            )

        getCurrencyRatesUseCase = GetCurrencyRatesUseCase(
            ratesRepositoryImplMocked,
            Schedulers.trampoline(),
            Schedulers.trampoline()
        )

        val currencyRateUSDEUR = CurrencyRate(
            "USD", "EUR",
            0.8, 12345L.toString()
        )
        val currencyRatesUSDEUR = CurrencyRates(
            listOf(currencyRateUSDEUR)
        )
        val goodResult = Result.success(currencyRatesUSDEUR)

        val observer = TestObserver<Result<CurrencyRates>>()
        getCurrencyRatesUseCase.execute(
            observer,
            {
                Log.i(
                    "GetCurrencyRatesUseCaseTest",
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
        verify(ratesRepositoryImplMocked).getRates()
        verifyNoMoreInteractions(ratesRepositoryImplMocked)

        getCurrencyRatesUseCase.dispose()
    }

}