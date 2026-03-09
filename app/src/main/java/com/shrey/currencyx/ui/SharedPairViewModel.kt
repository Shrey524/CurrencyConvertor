package com.shrey.currencyx.ui

import androidx.lifecycle.ViewModel
import com.shrey.currencyx.domain.model.Currency
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class CurrencyPair(
    val from: Currency,
    val to: Currency
)

@HiltViewModel
class SharedPairViewModel @Inject constructor() : ViewModel() {

    private val _pair = MutableStateFlow(
        CurrencyPair(
            from = Currency.getDefault("USD"),
            to = Currency.getDefault("INR")
        )
    )
    val pair: StateFlow<CurrencyPair> = _pair.asStateFlow()

    fun setPair(from: Currency, to: Currency) {
        _pair.value = CurrencyPair(from, to)
    }
}

