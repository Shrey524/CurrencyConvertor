package com.shrey.currencyx.ui.converter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrey.currencyx.domain.model.Currency
import com.shrey.currencyx.domain.usecase.GetExchangeRatesUseCase
import com.shrey.currencyx.util.Resource
import com.shrey.currencyx.util.toRelativeTimeString
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConverterViewModel @Inject constructor(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConverterUiState())
    val uiState = _uiState.asStateFlow()

    private val _favorites = mutableSetOf<Pair<String, String>>()

    init {
        loadExchangeRates()
    }

    fun onAmountChange(amount: String) {
        _uiState.update { state -> state.copy(amount = amount) }
    }

    fun onFromCurrencyChange(currency: Currency) {
        _uiState.update { state -> state.copy(fromCurrency = currency, showFromPicker = false) }
        updateFavoriteState()
    }

    fun onToCurrencyChange(currency: Currency) {
        _uiState.update { state -> state.copy(toCurrency = currency, showToPicker = false) }
        updateFavoriteState()
    }

    fun onSwapCurrencies() {
        _uiState.update { state ->
            state.copy(
                fromCurrency = state.toCurrency,
                toCurrency = state.fromCurrency,
                isSwapping = true
            )
        }
        updateFavoriteState()
        viewModelScope.launch {
            kotlinx.coroutines.delay(300)
            _uiState.update { state -> state.copy(isSwapping = false) }
        }
    }

    fun onToggleFavorite() {
        val pair = Pair(
            _uiState.value.fromCurrency.code,
            _uiState.value.toCurrency.code
        )
        if (pair in _favorites) {
            _favorites.remove(pair)
        } else {
            _favorites.add(pair)
        }
        _uiState.update { it.copy(isFavorite = pair in _favorites) }
    }

    fun onRefresh() {
        loadExchangeRates()
    }

    fun onShowFromPicker(show: Boolean) {
        _uiState.update { state -> state.copy(showFromPicker = show) }
    }

    fun onShowToPicker(show: Boolean) {
        _uiState.update { state -> state.copy(showToPicker = show) }
    }

    private fun loadExchangeRates() {
        viewModelScope.launch {
            getExchangeRatesUseCase("USD").collect { result ->
                when (result) {
                    is Resource.Loading -> _uiState.update { state -> state.copy(isLoading = true, error = null) }
                    is Resource.Success -> _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            exchangeRates = result.data,
                            error = null,
                            lastUpdated = result.data.lastUpdated.toRelativeTimeString()
                        )
                    }
                    is Resource.Error -> _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                updateFavoriteState()
            }
        }
    }

    private fun updateFavoriteState() {
        val state = _uiState.value
        val pair = Pair(state.fromCurrency.code, state.toCurrency.code)
        _uiState.update { it.copy(isFavorite = pair in _favorites) }
    }
}
