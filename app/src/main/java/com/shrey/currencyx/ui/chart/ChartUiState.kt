package com.shrey.currencyx.ui.chart

import com.shrey.currencyx.domain.model.ChartData
import com.shrey.currencyx.domain.model.ChartPeriod
import com.shrey.currencyx.domain.model.ChartStats
import com.shrey.currencyx.domain.model.Currency

data class ChartUiState(
    val fromCurrency: Currency = Currency.getDefault("USD"),
    val toCurrency: Currency = Currency.getDefault("INR"),
    val selectedPeriod: ChartPeriod = ChartPeriod.ONE_MONTH,
    val chartData: ChartData? = null,
    val stats: ChartStats? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val showFromPicker: Boolean = false,
    val showToPicker: Boolean = false
)
