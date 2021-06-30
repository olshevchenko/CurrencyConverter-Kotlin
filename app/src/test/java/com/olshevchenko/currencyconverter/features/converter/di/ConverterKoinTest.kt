package com.olshevchenko.currencyconverter.features.converter.di

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.olshevchenko.currencyconverter.core.CurrencyDateTime
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.data.RatesRepositoryImpl
import com.olshevchenko.currencyconverter.core.data.entity.RatesDataEntity
import com.olshevchenko.currencyconverter.features.converter.domain.usecase.RefreshCurrencyRatesUseCase
import com.olshevchenko.currencyconverter.features.converter.presentation.ConverterViewModel
import com.olshevchenko.currencyconverter.features.converter.presentation.RefreshPresenter
import com.olshevchenko.currencyconverter.features.converter.presentation.model.RefreshTimeStampToUIMapper
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verifySequence
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.component.inject
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.logger.Level
import org.koin.dsl.koinApplication
import org.koin.test.get


class ConverterKoinTest : KoinTest {

    private val ratesRepositoryImplMocked = mockk<RatesRepositoryImpl>()
    private val ratesRepositoryFailImplMocked = mockk<RatesRepositoryImpl>()

    private val slotBoolean = slot<Boolean>()
    private val slotString = slot<String>()
    private val stateObserver = mockk<Observer<Boolean>> {
        every {
            onChanged(capture(slotBoolean))
        } answers {
            Log.i(
                "ConverterKoinTest",
                "Got stateObserver.onChanged(${slotBoolean.captured})"
            )
        }
    }
    private val dateObserver = mockk<Observer<String>> {
        every {
            onChanged(capture(slotString))
        } answers {
            Log.i(
                "ConverterKoinTest",
                "Got dateObserver.onChanged(${slotString.captured})"
            )
        }
    }
    val someErrorObserver = mockk<Observer<String?>> { every { onChanged(any()) } answers {} }


    private val subscribeScheduler = Schedulers.trampoline()
    private val observeScheduler = Schedulers.trampoline()

    /**
     * create presentation instances (by inject()-)
     */
    private val converterViewModel: ConverterViewModel by inject(
        named("converterViewModelKoinTest"),
        mode = LazyThreadSafetyMode.NONE,
    )
    private val refreshPresenter: RefreshPresenter by inject(
        named("refreshPresenterKoinTest"),
        mode = LazyThreadSafetyMode.NONE,
    ) {
        parametersOf(
            get<RefreshCurrencyRatesUseCase>(
                named("refreshCurrencyRatesUseCaseKoinTest")
            ),
            converterViewModel,
            RefreshTimeStampToUIMapper
        )
    }

    /**
     * Koin modules for domain-level logic
     */
    val useCaseModulesKoinTest = module {
        single(named("refreshCurrencyRatesUseCaseKoinTest")) {
            RefreshCurrencyRatesUseCase(
                ratesRepository = ratesRepositoryImplMocked,
                subscribeScheduler,
                observeScheduler
            )
        }
    }
    /**
     * Koin modules for presentation-level (viewmodels & presenters)
     */
    val presentationModulesKoinTest = module {
        viewModel(named("converterViewModelKoinTest")) {
            ConverterViewModel()
        }
        factory(named("refreshPresenterKoinTest")) {
            RefreshPresenter(
                useCase = get(
                    named("refreshCurrencyRatesUseCaseKoinTest")
                ),
                viewModel = converterViewModel,
                RefreshTimeStampToUIMapper
            )
        }
    }

    val homeModulesKoinTest = listOf(
        useCaseModulesKoinTest,
        presentationModulesKoinTest,
    )

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    val initDateValue = CurrencyDateTime.UI_DATETIME_UNKNOWN
    val inProgressDateValue = CurrencyDateTime.UI_DATETIME_IN_PROGRESS

    val goodDateLong = 123456789L
    val goodResult = Result.success(goodDateLong)
    val goodDate = RefreshTimeStampToUIMapper.map(goodDateLong)

    val badDateLong = 0L
    val networkErrDescr = "network err"
    val badResult = Result.errorWData(
        badDateLong,
        errorDesc = networkErrDescr
    )
    val badDate = RefreshTimeStampToUIMapper.map(badDateLong)

    private val rateUSDEURQuote = RatesDataEntity.Quote(goodDateLong, 0.8)
    val ratesUSDEUREntity = RatesDataEntity(mapOf(Pair("USDEUR", rateUSDEURQuote)))
    val ratesEmptyEntity = RatesDataEntity(mapOf())


    @Before
    fun setUp() {
        every { ratesRepositoryImplMocked.refreshRates() } returns
                Single.just(goodResult)
        every { ratesRepositoryFailImplMocked.refreshRates() } returns
                Single.just(badResult)

        startKoin {
            androidLogger(Level.DEBUG)
            modules(
                homeModulesKoinTest
            )
        }
    }

    @After
    fun tearDown() {
        refreshPresenter.dispose()
        stopKoin()
    }

    @Test
//    fun verifyCorrectStartByKoin() {
    fun `should correctly create lazy viewModel and presenter instances by Koin`() {
        converterViewModel.isRefreshingState.observeForever(stateObserver)
        converterViewModel.refreshDate.observeForever(dateObserver)

        Log.i(
            "ConverterKoinTest",
            "======= Gonna start refreshPresenter.init()"
        )
        refreshPresenter.init()

        Log.i(
            "ConverterKoinTest",
            "======= Gonna start refreshPresenter.onRefreshRatesAction()"
        )
        refreshPresenter.onRefreshRatesAction()

        verifySequence {
            // set by viewModel instantiation
            stateObserver.onChanged(false)
            // set by presenter.init()
            stateObserver.onChanged(true) // -""- by setRefreshViewState(init())
            stateObserver.onChanged(true) // by setRefreshViewState(inProgress())
            stateObserver.onChanged(false) //by setRefreshViewState(success())
            stateObserver.onChanged(false) //by setRefreshViewState(ready())
            // set by presenter.onRefreshRatesAction()
            stateObserver.onChanged(true) // by setRefreshViewState(inProgress())
            stateObserver.onChanged(false) //by setRefreshViewState(success())
            stateObserver.onChanged(false) //by setRefreshViewState(ready())
        }

        verifySequence {
            // set by viewModel instantiation
            dateObserver.onChanged(initDateValue)
            // set by presenter.init()
            dateObserver.onChanged(initDateValue)       // -""- by setRefreshViewState(init())
            dateObserver.onChanged(inProgressDateValue) // by setRefreshViewState(inProgress())
            dateObserver.onChanged(goodDate)            // by setRefreshViewState(success())
            // set by presenter.onRefreshRatesAction()
            dateObserver.onChanged(inProgressDateValue) // by setRefreshViewState(inProgress())
            dateObserver.onChanged(goodDate)            // by setRefreshViewState(success())
        }
    }
}

