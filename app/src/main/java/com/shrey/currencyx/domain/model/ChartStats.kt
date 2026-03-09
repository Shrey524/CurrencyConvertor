package com.shrey.currencyx.domain.model

data class ChartStats(
    val currentRate: Double,
    val highRate: Double,
    val lowRate: Double,
    val averageRate: Double,
    val changePercent: Double,
    val changeAbsolute: Double,
    val isPositive: Boolean
)
