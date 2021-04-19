package com.olshevchenko.currencyconverter.core.data

//import com.olshevchenko.currencyconverter.features.operations.domain.model.ExchangeOperation
//import com.olshevchenko.currencyconverter.features.operations.domain.repository.ExchangeOperationsRepository
//import io.reactivex.Observable
//
//class ExchangeOperationDataRepository(private val repository: ExchangeOperationsRepository) {
//
//    suspend fun addExchangeOperation(operation: ExchangeOperation) =
//            repository.add(operation) //TODO remake coroutine "suspend" to someone from rxJava!!!
//
//    suspend fun removeExchangeOperation(operation: ExchangeOperation) =
//            repository.remove(operation) //TODO remake coroutine "suspend" to someone from rxJava!!!
//
//    fun readAllExchangeOperations(): Observable<List<ExchangeOperation>> =
//        repository.readAll()
//
//    suspend fun removeAllExchangeOperations() =
//            repository.removeAll() //TODO remake coroutine "suspend" to someone from rxJava!!!
//}