package com.shrey.currencyx.data.repository

import com.shrey.currencyx.data.remote.FrankfurterApiService
import com.shrey.currencyx.domain.model.ChartData
import com.shrey.currencyx.domain.model.ChartPoint
import com.shrey.currencyx.domain.model.ChartPeriod
import com.shrey.currencyx.domain.model.ChartStats
import com.shrey.currencyx.domain.repository.ChartRepository
import com.shrey.currencyx.util.LogUtil
import com.shrey.currencyx.util.Resource
import com.shrey.currencyx.util.getDateString
import com.shrey.currencyx.util.toEpochMillis
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChartRepositoryImpl @Inject constructor(
    private val api: FrankfurterApiService
) : ChartRepository {

    override suspend fun getHistoricalRates(
        from: String,
        to: String,
        period: ChartPeriod
    ): Resource<ChartData> {
        return try {
            val endDate = getDateString(0)
            val startDate = getDateString(period.days)

            val response = api.getHistoricalRates(
                startDate = startDate,
                endDate = endDate,
                from = from,
                to = to
            )

            val points = response.rates.mapNotNull { (date, rates) ->
                val rate = rates[to]

                if (rate != null && rate.isFinite() && rate > 0) {
                    ChartPoint(
                        date = date,
                        timestamp = date.toEpochMillis(),
                        rate = rate
                    )
                } else {
                    null
                }
            }.sortedBy { it.timestamp }

            if (points.isEmpty()) {
                return Resource.Error("No valid exchange rate data")
            }

            Resource.Success(
                ChartData(
                    points = points,
                    fromCurrency = from,
                    toCurrency = to,
                    period = period
                )
            )
        } catch (e: Exception) {
            LogUtil.e("Error fetching chart rates", e)
            Resource.Error(e.message ?: "Failed to fetch historical data")
        }
    }

    override fun calculateStats(chartData: ChartData): ChartStats {
        val points = chartData.points
        if (points.isEmpty()) {
            return ChartStats(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, false)
        }

        val rates = points.map { it.rate }
        val firstRate = points.first().rate
        val lastRate = points.last().rate
        val changeAbsolute = lastRate - firstRate
        val changePercent = if (firstRate != 0.0) (changeAbsolute / firstRate) * 100 else 0.0

        return ChartStats(
            currentRate = lastRate,
            highRate = rates.maxOrNull() ?: 0.0,
            lowRate = rates.minOrNull() ?: 0.0,
            averageRate = rates.average(),
            changePercent = changePercent,
            changeAbsolute = changeAbsolute,
            isPositive = changeAbsolute >= 0
        )
    }
}
