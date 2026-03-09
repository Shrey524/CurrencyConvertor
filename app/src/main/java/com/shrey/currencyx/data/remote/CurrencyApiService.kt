package com.shrey.currencyx.data.remote

import com.shrey.currencyx.BuildConfig
import com.shrey.currencyx.data.remote.dto.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApiService {
    @GET("v6/{apiKey}/latest/{baseCurrency}")
    suspend fun getExchangeRates(
        @Path("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Path("baseCurrency") baseCurrency: String
    ): ExchangeRateResponse
}
