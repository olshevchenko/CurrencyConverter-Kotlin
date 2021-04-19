package com.olshevchenko.currencyconverter.features.operations.domain.usecase

import com.olshevchenko.currencyconverter.features.operations.domain.model.ExchangeOperation
import com.olshevchenko.currencyconverter.features.operations.domain.repository.ExchangeOperationsRepository

class SaveExchangeOperationUseCase(private val operationsRepository: ExchangeOperationsRepository) {
    fun execute(operation: ExchangeOperation) =
        operationsRepository.add(operation)
}
