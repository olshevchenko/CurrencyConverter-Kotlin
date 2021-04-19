package com.olshevchenko.currencyconverter.features.operations.domain.repository

import com.olshevchenko.currencyconverter.features.operations.domain.model.ExchangeOperation
import com.olshevchenko.currencyconverter.features.operations.domain.model.ExchangeOperations
import io.reactivex.Observable;

interface ExchangeOperationsRepository {

    fun add(operation: ExchangeOperation) //TODO ensure the fun isn't asynchronous

    fun remove(id: Int): Boolean //TODO ensure the fun isn't asynchronous

//    fun read(): Observable<ExchangeOperation>

    fun readAll(): Observable<ExchangeOperations>

    fun clear() //TODO ensure the fun isn't asynchronous
}