package com.shrey.currencyx.ui.charts

import com.shrey.currencyx.domain.model.ChartPeriod
import com.shrey.currencyx.domain.model.ChartPoint

data class ChartUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val fromCurrency: String = "USD",
    val toCurrency: String = "INR",
    val currentRate: Double = 0.0,
    val changePercent: Double = 0.0,
    val isPositive: Boolean = true,
    val selectedPeriod: ChartPeriod = ChartPeriod.ONE_MONTH,
    val chartData: List<ChartPoint> = emptyList(),
    val highRate: Double = 0.0,
    val lowRate: Double = 0.0,
    val averageRate: Double = 0.0,
    val showFromPicker: Boolean = false,
    val showToPicker: Boolean = false
)
