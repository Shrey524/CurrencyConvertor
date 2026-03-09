package com.shrey.currencyx.domain.repository

import com.shrey.currencyx.domain.model.ChartData
import com.shrey.currencyx.domain.model.ChartPeriod
import com.shrey.currencyx.domain.model.ChartStats
import com.shrey.currencyx.util.Resource

interface ChartRepository {
    suspend fun getHistoricalRates(
        from: String,
        to: String,
        period: ChartPeriod
    ): Resource<ChartData>

    fun calculateStats(chartData: ChartData): ChartStats
}
