package com.shrey.currencyx.data.repository

import com.shrey.currencyx.data.local.CurrencyDao
import com.shrey.currencyx.data.local.entity.CachedRateEntity
import com.shrey.currencyx.data.mapper.toExchangeRate
import com.shrey.currencyx.data.remote.CurrencyApiService
import com.shrey.currencyx.domain.model.ExchangeRate
import com.shrey.currencyx.domain.repository.CurrencyRepository
import com.shrey.currencyx.util.LogUtil
import com.shrey.currencyx.util.Resource
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepositoryImpl @Inject constructor(
    private val api: CurrencyApiService,
    private val dao: CurrencyDao,
    private val json: Json
) : CurrencyRepository {

    override suspend fun getExchangeRates(baseCurrency: String): Resource<ExchangeRate> {
        return try {
            val response = api.getExchangeRates(baseCurrency = baseCurrency)
            val ratesJson = json.encodeToString(response.conversionRates)
            dao.insertCachedRates(
                CachedRateEntity(
                    baseCurrency = baseCurrency,
                    ratesJson = ratesJson,
                    lastUpdated = response.timeLastUpdateUnix
                )
            )
            Resource.Success(response.toExchangeRate())
        } catch (e: Exception) {
            LogUtil.e("API failed, using cache", e)
            val cached = getCachedRates()
            if (cached != null) Resource.Success(cached)
            else Resource.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun getCachedRates(): ExchangeRate? {
        return dao.getCachedRates("USD")?.let { entity ->
            val rates = json.decodeFromString<Map<String, Double>>(entity.ratesJson)
            ExchangeRate(
                baseCurrency = entity.baseCurrency,
                rates = rates,
                lastUpdated = entity.lastUpdated
            )
        }
    }
}
