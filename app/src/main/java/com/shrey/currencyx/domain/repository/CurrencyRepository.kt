package com.shrey.currencyx.domain.repository

import com.shrey.currencyx.domain.model.ExchangeRate
import com.shrey.currencyx.util.Resource

/** Contract for exchange rates and cache. */
interface CurrencyRepository {
    suspend fun getExchangeRates(baseCurrency: String): Resource<ExchangeRate>
    suspend fun getCachedRates(): ExchangeRate?
}
