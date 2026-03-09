package com.shrey.currencyx.data.repository

import android.util.Log
import com.shrey.currencyx.data.remote.FrankfurterApiService
import com.shrey.currencyx.domain.model.ChartData
import com.shrey.currencyx.domain.model.ChartPoint
import com.shrey.currencyx.domain.model.ChartPeriod
import com.shrey.currencyx.domain.model.ChartStats
import com.shrey.currencyx.domain.repository.ChartRepository
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

            Log.d("ChartRepo", "Fetching $from -> $to from $startDate to $endDate")

            val response = api.getHistoricalRates(
                startDate = startDate,
                endDate = endDate,
                from = from,
                to = to
            )

            Log.d("ChartRepo", "Response rates count: ${response.rates.size}")

            val points = response.rates.mapNotNull { (date, rates) ->
                val rate = rates[to]
                Log.d("ChartRepo", "Date: $date, Rate: $rate")

                if (rate != null && rate.isFinite() && rate > 0) {
                    ChartPoint(
                        date = date,
                        timestamp = date.toEpochMillis(),
                        rate = rate
                    )
                } else {
                    Log.w("ChartRepo", "Skipping invalid rate: $rate for date $date")
                    null
                }
            }.sortedBy { it.timestamp }

            Log.d("ChartRepo", "Valid points: ${points.size}")

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
            Log.e("ChartRepo", "Error fetching rates", e)
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
