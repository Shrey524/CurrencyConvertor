package com.shrey.currencyx.domain.usecase

import com.shrey.currencyx.domain.model.ChartData
import com.shrey.currencyx.domain.model.ChartStats
import com.shrey.currencyx.domain.repository.ChartRepository
import javax.inject.Inject

/** Calculates display statistics from chart data. */
class GetChartStatsUseCase @Inject constructor(
    private val repository: ChartRepository
) {
    operator fun invoke(chartData: ChartData): ChartStats {
        return repository.calculateStats(chartData)
    }
}
