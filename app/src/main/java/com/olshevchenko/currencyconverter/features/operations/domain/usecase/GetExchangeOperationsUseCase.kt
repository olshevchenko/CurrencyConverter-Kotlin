package com.olshevchenko.currencyconverter.features.operations.domain.usecase

import com.olshevchenko.currencyconverter.features.operations.domain.model.ExchangeOperations
import com.olshevchenko.currencyconverter.features.operations.domain.repository.ExchangeOperationsRepository
import com.olshevchenko.currencyconverter.core.usecase.RXUseCase
import io.reactivex.Scheduler
import io.reactivex.Single

//class GetExchangeOperationsUseCase @Inject constructor(
//    private val operationsRepository: ExchangeOperationsRepository,
//    subscribeScheduler: Scheduler,
//    postExecutionScheduler: Scheduler) :
//        RXUseCase<ExchangeOperations, Unit>(subscribeScheduler, postExecutionScheduler) {
//
//    override fun buildUseCaseSingle(params: Unit?): Single<ExchangeOperations> = operationsRepository.readAll()
//            .map {
//                it.map { Article(it.title, it.pubDate, it.link) }
//            }
//}
