package com.shrey.currencyx.domain.model

data class ChartData(
    val points: List<ChartPoint>,
    val fromCurrency: String,
    val toCurrency: String,
    val period: ChartPeriod
)
