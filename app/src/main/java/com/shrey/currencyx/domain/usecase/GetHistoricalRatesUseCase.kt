package com.shrey.currencyx.domain.usecase

import com.shrey.currencyx.domain.model.ChartData
import com.shrey.currencyx.domain.model.ChartPeriod
import com.shrey.currencyx.domain.repository.ChartRepository
import com.shrey.currencyx.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetHistoricalRatesUseCase @Inject constructor(
    private val repository: ChartRepository
) {
    suspend operator fun invoke(
        from: String,
        to: String,
        period: ChartPeriod
    ): Flow<Resource<ChartData>> = flow {
        emit(Resource.Loading())
        emit(repository.getHistoricalRates(from, to, period))
    }
}
