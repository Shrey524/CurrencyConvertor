package com.shrey.currencyx.data.remote

import com.shrey.currencyx.data.remote.dto.FrankfurterHistoricalResponse
import com.shrey.currencyx.data.remote.dto.FrankfurterLatestResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FrankfurterApiService {

    @GET("latest")
    suspend fun getLatestRates(
        @Query("from") from: String = "USD",
        @Query("to") to: String? = null
    ): FrankfurterLatestResponse

    @GET("{startDate}..{endDate}")
    suspend fun getHistoricalRates(
        @Path("startDate") startDate: String,
        @Path("endDate") endDate: String,
        @Query("from") from: String,
        @Query("to") to: String
    ): FrankfurterHistoricalResponse
}
