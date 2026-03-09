package com.shrey.currencyx.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FrankfurterLatestResponse(
    val amount: Double,
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)

@Serializable
data class FrankfurterHistoricalResponse(
    val amount: Double,
    val base: String,
    @SerialName("start_date") val startDate: String,
    @SerialName("end_date") val endDate: String,
    val rates: Map<String, Map<String, Double>>
)
