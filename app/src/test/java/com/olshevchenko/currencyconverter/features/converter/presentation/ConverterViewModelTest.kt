package com.olshevchenko.currencyconverter.features.converter.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.olshevchenko.currencyconverter.core.CurrencyDateTime
import com.olshevchenko.currencyconverter.core.Result
import com.olshevchenko.currencyconverter.core.presentation.RefreshViewState
import com.olshevchenko.currencyconverter.features.converter.presentation.model.RefreshTimeStampToUIMapper
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ConverterViewModelTest {

    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    val refreshTimeStampToUIMapperMocked = mockk<RefreshTimeStampToUIMapper>()

    val stateObserver = mockk<Observer<Boolean>> { every { onChanged(any()) } answers {} }
    val dateObserver = mockk<Observer<String>> { every { onChanged(any()) } answers {} }
    val someErrorObserver = mockk<Observer<String?>> { every { onChanged(any()) } answers {} }

    private lateinit var viewModel: ConverterViewModel

    val initDateValue = CurrencyDateTime.UI_DATETIME_UNKNOWN
    val inProgressDateValue = CurrencyDateTime.UI_DATETIME_IN_PROGRESS

    val goodResult = Result.success(12345L)
    val goodDate = "2021-01-01 12:34:50"

    val networkErrDescr = "network err"
    val badResult = Result.errorWData(
        0L,
        errorDesc = networkErrDescr
    )
    val badDate = "2021-01-01 00:00:00"

    @Before
    fun setUp() {

        every { refreshTimeStampToUIMapperMocked.map(12345L) } returns goodDate
        every { refreshTimeStampToUIMapperMocked.map(0L) } returns badDate

        viewModel = ConverterViewModel()
    }

    @After
    fun tearDown() {
    }

    @Test
    fun setCodesViewState() {
    }

    @Test
    fun setRateViewState() {
    }

    @Test
    //    fun verifySuccessfullRefreshViewStateSetting() {
    fun `should correctly update both refreshing state and refresh date LiveData values for successf refresh`() {
        viewModel.isRefreshingState.observeForever(stateObserver)
        viewModel.refreshDate.observeForever(dateObserver)

        viewModel.setRefreshViewState(
            RefreshViewState.success(goodDate)
        )

        verifySequence {
            stateObserver.onChanged(false) // set by viewModel instantiation
            stateObserver.onChanged(false) // -""- by setRefreshViewState() above
        }

        verifySequence {
            dateObserver.onChanged(initDateValue) // by instantiation
            dateObserver.onChanged(goodDate)      // by setRefreshViewState() above
        }
    }

    @Test
    //    fun verifyFailedRefreshViewStateSetting() {
    fun `should update refreshing state, refresh date and gotSomeError LiveData values for faulty refresh`() {
        viewModel.isRefreshingState.observeForever(stateObserver)
        viewModel.refreshDate.observeForever(dateObserver)
        viewModel.gotSomeError.observeForever(someErrorObserver)

        viewModel.setRefreshViewState(
            RefreshViewState.error(
                badDate, errorDesc = networkErrDescr
            )
        )

        verifySequence {
            stateObserver.onChanged(false) // set by viewModel instantiation
            stateObserver.onChanged(false) // -""- by setRefreshViewState() above
        }

        verifySequence {
            dateObserver.onChanged(initDateValue)
            dateObserver.onChanged(badDate)
        }

        verifySequence {
            someErrorObserver.onChanged(null)
            someErrorObserver.onChanged(networkErrDescr)
        }
    }

    @Test
    //    fun verifyRefreshViewStateSequence() {
    fun `should correctly emulate  Refresh result liveData values changes according to presenter calls`() {
    // should restore backup refreshDate value after error
        viewModel.isRefreshingState.observeForever(stateObserver)
        viewModel.refreshDate.observeForever(dateObserver)

        viewModel.setRefreshViewState(RefreshViewState.init())
        viewModel.setRefreshViewState(RefreshViewState.inProgress())
        viewModel.setRefreshViewState(RefreshViewState.success(goodDate))
        viewModel.setRefreshViewState(RefreshViewState.inProgress())
        viewModel.setRefreshViewState(RefreshViewState.error(errorDesc = networkErrDescr))

        verifySequence {
            stateObserver.onChanged(false) // set by viewModel instantiation
            stateObserver.onChanged(true) // -""- by setRefreshViewState(init())
            stateObserver.onChanged(true) // by setRefreshViewState(inProgress())
            stateObserver.onChanged(false) //by setRefreshViewState(success())
            stateObserver.onChanged(true) // true again - by setRefreshViewState(inProgress())
            stateObserver.onChanged(false) //by setRefreshViewState(error())
        }

        verifySequence {
            dateObserver.onChanged(initDateValue) // set by viewModel instantiation
            dateObserver.onChanged(initDateValue) // -""- by setRefreshViewState(init())
            dateObserver.onChanged(inProgressDateValue) // by setRefreshViewState(inProgress())
            dateObserver.onChanged(goodDate) // by setRefreshViewState(success())
            dateObserver.onChanged(inProgressDateValue) // by setRefreshViewState(inProgress())
            dateObserver.onChanged(goodDate) // restore backup state - after setRefreshViewState(error())
        }

    }

}