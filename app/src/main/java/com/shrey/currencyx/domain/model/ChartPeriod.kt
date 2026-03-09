package com.shrey.currencyx.domain.model

enum class ChartPeriod(val label: String, val days: Int) {
    ONE_DAY("1D", 1),
    FIVE_DAYS("5D", 5),
    ONE_MONTH("1M", 30),
    ONE_YEAR("1Y", 365)
}
