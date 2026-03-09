package com.shrey.currencyx.ui.converter

import com.shrey.currencyx.domain.model.Currency
import com.shrey.currencyx.domain.model.ExchangeRate

data class ConverterUiState(
    val amount: String = "1000",
    val fromCurrency: Currency = Currency.getDefault("USD"),
    val toCurrency: Currency = Currency.getDefault("INR"),
    val exchangeRates: ExchangeRate? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val isFavorite: Boolean = false,
    val isSwapping: Boolean = false,
    val showFromPicker: Boolean = false,
    val showToPicker: Boolean = false,
    val lastUpdated: String = ""
) {
    val convertedAmount: Double
        get() {
            val from = exchangeRates?.rates?.get(fromCurrency.code) ?: 1.0
            val to = exchangeRates?.rates?.get(toCurrency.code) ?: 1.0
            val amt = amount.toDoubleOrNull()?.coerceAtMost(1_000_000_000.0) ?: 0.0
            return amt * (to / from)
        }

    val exchangeRateText: String
        get() {
            val from = exchangeRates?.rates?.get(fromCurrency.code) ?: 1.0
            val to = exchangeRates?.rates?.get(toCurrency.code) ?: 1.0
            return "1 ${fromCurrency.code} = ${String.format("%.4f", to / from)} ${toCurrency.code}"
        }
}
