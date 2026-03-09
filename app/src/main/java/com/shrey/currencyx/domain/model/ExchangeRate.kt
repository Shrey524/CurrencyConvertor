package com.shrey.currencyx.domain.model

data class ExchangeRate(
    val baseCurrency: String,
    val rates: Map<String, Double>,
    val lastUpdated: Long
)
