package com.shrey.currencyx.ui.charts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shrey.currencyx.domain.model.ChartPeriod
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
class ChartsViewModel @Inject constructor(
    private val getHistoricalRatesUseCase: GetHistoricalRatesUseCase,
    private val getChartStatsUseCase: GetChartStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChartUiState())
    val uiState: StateFlow<ChartUiState> = _uiState.asStateFlow()

    init {
        loadChartData()
    }

    fun selectPeriod(period: ChartPeriod) {
        _uiState.update { it.copy(selectedPeriod = period) }
        loadChartData()
    }

    fun swapCurrencies() {
        _uiState.update {
            it.copy(
                fromCurrency = it.toCurrency,
                toCurrency = it.fromCurrency
            )
        }
        loadChartData()
    }

    fun selectFromCurrency(code: String) {
        _uiState.update { it.copy(fromCurrency = code, showFromPicker = false) }
        loadChartData()
    }

    fun selectToCurrency(code: String) {
        _uiState.update { it.copy(toCurrency = code, showToPicker = false) }
        loadChartData()
    }

    fun onShowFromPicker(show: Boolean) {
        _uiState.update { it.copy(showFromPicker = show) }
    }

    fun onShowToPicker(show: Boolean) {
        _uiState.update { it.copy(showToPicker = show) }
    }

    fun refresh() {
        loadChartData()
    }

    private fun loadChartData() {
        viewModelScope.launch {
            val state = _uiState.value
            _uiState.update { it.copy(isLoading = true, error = null) }

            getHistoricalRatesUseCase(
                from = state.fromCurrency,
                to = state.toCurrency,
                period = state.selectedPeriod
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        val data = result.data
                        val stats = getChartStatsUseCase(data)
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                chartData = data.points,
                                currentRate = stats.currentRate,
                                changePercent = stats.changePercent,
                                isPositive = stats.isPositive,
                                highRate = stats.highRate,
                                lowRate = stats.lowRate,
                                averageRate = stats.averageRate,
                                error = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.message ?: "Failed to load chart data"
                            )
                        }
                    }
                }
            }
        }
    }
}
