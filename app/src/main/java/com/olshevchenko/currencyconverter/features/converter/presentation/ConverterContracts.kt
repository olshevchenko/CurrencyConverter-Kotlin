package com.olshevchenko.currencyconverter.features.converter.presentation

import com.olshevchenko.currencyconverter.core.presentation.ViewState
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate

/**
 *  Actions for ALL control flows (UI->presenters->domain):
 * 1) GetCodes - to get list of currency codes
 * 2) GetRate - to get rate for selected currencies
 * 3) (SwipeTo/PushTo) RefreshRates - to refresh all currency rates
 * 4) SaveRates - to locally save all existed currency rates
 *
 *  Notifications for ALL data flows (viewmodel->view)
 * 1) RefreshingStateReceived - to show/hide rates refresh progress
 * 2) CodesLoadingStateReceived - to show/hide rates codes list
 * 3) GettingRateErrorReceived - to show/hide error of getting currency rate
 *
 *  States of ALL data flows (domain->presenters->viewmodel->view) (see [core/presentation/ViewState.kt]):
 * 1) CodesLoad[ing/ed] (got list of all existed currency codes)
 * 2) RateReceiv[ing/ed] (got new rate for selected pair of currencies or "rate not found" error)
 * 3) RatesRefresh[ing/ed] (all rates are refreshed with success or some of network error)
 * 4) RatesSav[ing/ed] (all rates are saved locally with success or some of local error)
 */

interface ConverterContracts {

    interface View {
        fun initView()
//        fun onRefreshingStateReceived(isRefreshing: Boolean)
        fun onCodesLoadingStateReceived(isLoading: Boolean)
        fun onSomeErrorReceived(errMsg: String? = null)
    }

    interface Presenter {
        fun init()
        fun dispose()
    }

    interface CodesPresenter: Presenter {
        fun onGetCodesAction() // process request for getting list of all available currencies
    }

    interface RatePresenter: Presenter {
        fun onGetRateAction(from: String, to: String) // process request for getting rate of the
                                                      // specified currency pair
    }

    interface RefreshPresenter: Presenter {
        fun onRefreshRatesAction() // process request for refreshing rates of all currencies
    }

    interface SavePresenter: Presenter {
        fun onSaveRatesAction() // process request for saving all executed currencies conversion operations
    }

}