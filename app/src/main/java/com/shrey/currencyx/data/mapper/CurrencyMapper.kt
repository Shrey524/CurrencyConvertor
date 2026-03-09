package com.shrey.currencyx.data.mapper

import com.shrey.currencyx.data.remote.dto.ExchangeRateResponse
import com.shrey.currencyx.domain.model.ExchangeRate

fun ExchangeRateResponse.toExchangeRate(): ExchangeRate = ExchangeRate(
    baseCurrency = baseCode,
    rates = conversionRates,
    lastUpdated = timeLastUpdateUnix
)
