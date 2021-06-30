package com.olshevchenko.currencyconverter.features.operations.presentation

import com.olshevchenko.currencyconverter.core.presentation.ViewState
import com.olshevchenko.currencyconverter.features.converter.domain.model.CurrencyRate

/**
 *  Event list from ALL Views:
 * 1) GetOperations - to get list of all operations saved
 * 2) SaveOperation - to store the recent operation of currencies conversion
 *
 * States of ALL Presenters (see [core/presentation/ViewState.kt]):
 * 1) OperationsLoad[ing/ed] (got list of all existed operations)
 * 2) OperationSav[ing/ed] (recent operation is saved locally with success or some of local error)
 */

interface OperationsContracts {

    interface Presenter {
        fun init()
        fun dispose()
    }

    interface OperationsListPresenter: Presenter {
        fun onGetOperationsAction() // process request for getting list of all operations saved earlier
    }

    interface SavePresenter: Presenter {
        fun onSaveOperationAction() // process request for saving the recent operation of currencies conversion
    }

}