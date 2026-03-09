package com.shrey.currencyx.domain.usecase

import com.shrey.currencyx.domain.model.ExchangeRate
import com.shrey.currencyx.domain.repository.CurrencyRepository
import com.shrey.currencyx.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repository: CurrencyRepository
) {
    operator fun invoke(baseCurrency: String): Flow<Resource<ExchangeRate>> = flow {
        emit(Resource.Loading())
        emit(repository.getExchangeRates(baseCurrency))
    }
}
