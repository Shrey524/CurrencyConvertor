package com.shrey.currencyx.ui.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrey.currencyx.domain.model.ChartPeriod
import com.shrey.currencyx.domain.model.Currency
import com.shrey.currencyx.domain.usecase.GetChartStatsUseCase
import com.shrey.currencyx.domain.usecase.GetHistoricalRatesUseCase
import com.shrey.currencyx.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChartViewModel @Inject constructor(
    private val getHistoricalRatesUseCase: GetHistoricalRatesUseCase,
    private val getChartStatsUseCase: GetChartStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChartUiState())
    val uiState: StateFlow<ChartUiState> = _uiState.asStateFlow()

    init {
        loadChartData()
    }

    fun onFromCurrencyChange(currency: Currency) {
        _uiState.update {
            it.copy(fromCurrency = currency, showFromPicker = false)
        }
        loadChartData()
    }

    fun onToCurrencyChange(currency: Currency) {
        _uiState.update {
            it.copy(toCurrency = currency, showToPicker = false)
        }
        loadChartData()
    }

    fun onPeriodChange(period: ChartPeriod) {
        _uiState.update { it.copy(selectedPeriod = period) }
        loadChartData()
    }

    fun onSwapCurrencies() {
        _uiState.update {
            it.copy(
                fromCurrency = it.toCurrency,
                toCurrency = it.fromCurrency
            )
        }
        loadChartData()
    }

    fun onShowFromPicker(show: Boolean) {
        _uiState.update { it.copy(showFromPicker = show) }
    }

    fun onShowToPicker(show: Boolean) {
        _uiState.update { it.copy(showToPicker = show) }
    }

    fun onRetry() {
        loadChartData()
    }

    private fun loadChartData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            getHistoricalRatesUseCase(
                from = _uiState.value.fromCurrency.code,
                to = _uiState.value.toCurrency.code,
                period = _uiState.value.selectedPeriod
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val stats = getChartStatsUseCase(result.data)
                        _uiState.update {
                            it.copy(
                                chartData = result.data,
                                stats = stats,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }
                    }
                }
            }
        }
    }
}
