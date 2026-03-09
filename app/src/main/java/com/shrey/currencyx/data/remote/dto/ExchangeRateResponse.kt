package com.shrey.currencyx.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ExchangeRateResponse(
    val result: String,
    @SerialName("base_code") val baseCode: String,
    @SerialName("conversion_rates") val conversionRates: Map<String, Double>,
    @SerialName("time_last_update_unix") val timeLastUpdateUnix: Long
)
