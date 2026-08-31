package com.shrey.currencyx.ui.chart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shrey.currencyx.domain.model.Currency
import com.shrey.currencyx.ui.chart.components.CurrencyPairSelector
import com.shrey.currencyx.ui.chart.components.ExchangeRateChart
import com.shrey.currencyx.ui.chart.components.PeriodSelector
import com.shrey.currencyx.ui.chart.components.StatsGrid
import com.shrey.currencyx.ui.components.CurrencyPicker
import com.shrey.currencyx.ui.theme.Emerald500

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartsScreen(
    viewModel: ChartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        initialValue = ChartUiState()
    )
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CurrencyPairSelector(
            fromCurrency = uiState.fromCurrency,
            toCurrency = uiState.toCurrency,
            onFromClick = { viewModel.onShowFromPicker(true) },
            onToClick = { viewModel.onShowToPicker(true) },
            onSwapClick = viewModel::onSwapCurrencies
        )

        PeriodSelector(
            selectedPeriod = uiState.selectedPeriod,
            onPeriodSelected = viewModel::onPeriodChange
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                uiState.stats?.let { stats ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "1 ${uiState.fromCurrency.code}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = String.format("%.4f %s", stats.currentRate, uiState.toCurrency.code),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        val changeColor = if (stats.isPositive) Emerald500 else Color(0xFFEF4444)
                        val changePrefix = if (stats.isPositive) "+" else ""
                        val changeIcon = if (stats.isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = changeColor.copy(alpha = 0.1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = changeIcon,
                                    contentDescription = null,
                                    tint = changeColor,
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "$changePrefix${String.format("%.2f", stats.changePercent)}%",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = changeColor,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                when {
                    uiState.isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    uiState.error != null -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = uiState.error ?: "Unknown error",
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                TextButton(onClick = viewModel::onRetry) {
                                    Text("Retry")
                                }
                            }
                        }
                    }
                    else -> {
                        // Only render chart if we have valid data
                        val chartData = uiState.chartData
                        val hasValidData = chartData != null &&
                                chartData.points.size >= 2 &&
                                chartData.points.all { it.rate.isFinite() && it.rate > 0 }

                        if (hasValidData) {
                            ExchangeRateChart(
                                chartData = chartData,
                                isPositive = uiState.stats?.isPositive ?: true
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Unable to display chart",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        uiState.stats?.let { stats ->
            StatsGrid(
                stats = stats,
                toCurrencyCode = uiState.toCurrency.code
            )
        }
    }

    if (uiState.showFromPicker) {
        CurrencyPicker(
            currencies = Currency.getFrankfurterSupported(),
            selectedCurrency = uiState.fromCurrency,
            onCurrencySelected = viewModel::onFromCurrencyChange,
            onDismiss = { viewModel.onShowFromPicker(false) }
        )
    }

    if (uiState.showToPicker) {
        CurrencyPicker(
            currencies = Currency.getFrankfurterSupported(),
            selectedCurrency = uiState.toCurrency,
            onCurrencySelected = viewModel::onToCurrencyChange,
            onDismiss = { viewModel.onShowToPicker(false) }
        )
    }
}
