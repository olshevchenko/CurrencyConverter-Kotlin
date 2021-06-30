package com.olshevchenko.currencyconverter.features.converter.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.olshevchenko.currencyconverter.core.CurrencyDateTime
import com.olshevchenko.currencyconverter.core.presentation.CodesViewState
import com.olshevchenko.currencyconverter.core.presentation.RateViewState
import com.olshevchenko.currencyconverter.core.presentation.RefreshViewState
import com.olshevchenko.currencyconverter.core.presentation.ViewState

class ConverterViewModel : ViewModel() {

    /**
     * LiveData pairs for codes list, rate value and refresh result fields of corresponding view
     */
    private val _currencyCodes: MutableLiveData<List<String>> =
        MutableLiveData(listOf())
    val currencyCodes: LiveData<List<String>>
        get() = _currencyCodes

    private val _currencyRate: MutableLiveData<Double> =
        MutableLiveData(1.0)
    val currencyRate: LiveData<Double>
        get() = _currencyRate

    private val _refreshDate: MutableLiveData<String> =
        MutableLiveData(CurrencyDateTime.UI_DATETIME_UNKNOWN)
    val refreshDate: LiveData<String>
        get() = _refreshDate

    // backup field for restoring date after failure
    private var _refreshDateBackupValue = _refreshDate.value


    /**
     * LiveData for states
     */
    private val _isCodesLoadingState: MutableLiveData<Boolean> = MutableLiveData(false)
    val isCodesLoadingState: LiveData<Boolean>
        get() = _isCodesLoadingState

    private val _isRefreshingState: MutableLiveData<Boolean> = MutableLiveData(false)
    val isRefreshingState: LiveData<Boolean>
        get() = _isRefreshingState

    private val _gotSomeError: MutableLiveData<String?> = MutableLiveData(null)
    val gotSomeError: LiveData<String?>
        get() = _gotSomeError

    /**
     * external setting functions for changing livedata by presenters
     */
    fun setCodesViewState(viewState: CodesViewState) {
    }

    fun setRateViewState(viewState: RateViewState) {
    }

    fun setRefreshViewState(viewState: RefreshViewState) {
        when (viewState.stateType) {
            ViewState.StateType.INIT -> {
                _isRefreshingState.value = true
                _refreshDate.value = CurrencyDateTime.UI_DATETIME_UNKNOWN
            }
            ViewState.StateType.IN_PROGRESS -> {
                _isRefreshingState.value = true
                _refreshDateBackupValue = _refreshDate.value
                _refreshDate.value = CurrencyDateTime.UI_DATETIME_IN_PROGRESS
            }
            ViewState.StateType.SUCCESS -> {
                _isRefreshingState.value = false
                _refreshDate.value = viewState.data
            }
            ViewState.StateType.ERROR -> {
                _isRefreshingState.value = false
                viewState.data?.let {
                    _refreshDate.value = it
                } ?: run {
                    _refreshDate.value = _refreshDateBackupValue
                }
                viewState.errorDesc?.let {
                    _gotSomeError.value = it
                }
            }
            ViewState.StateType.READY -> {
                _isRefreshingState.value = false
                // _refreshDate.value = ... keep the date value unchanged
            }
        }
    }

}