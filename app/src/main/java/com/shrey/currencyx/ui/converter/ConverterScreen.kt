package com.shrey.currencyx.ui.converter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shrey.currencyx.domain.model.Currency
import com.shrey.currencyx.ui.components.AmountInput
import com.shrey.currencyx.ui.components.CurrencyCard
import com.shrey.currencyx.ui.components.CurrencyPicker
import com.shrey.currencyx.ui.components.LoadingShimmer
import com.shrey.currencyx.ui.components.ResultCard
import com.shrey.currencyx.ui.components.SwapButton
import com.shrey.currencyx.ui.SharedPairViewModel
import androidx.compose.runtime.LaunchedEffect

@Composable
fun ConverterScreen(
    viewModel: ConverterViewModel = hiltViewModel(),
    onRegisterRefresh: ((() -> Unit)?) -> Unit = {}
) {
    val sharedPairViewModel: SharedPairViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle(
        initialValue = ConverterUiState()
    )
    val scrollState = rememberScrollState()

    DisposableEffect(Unit) {
        onRegisterRefresh(viewModel::onRefresh)
        onDispose { onRegisterRefresh(null) }
    }

    LaunchedEffect(uiState.fromCurrency.code, uiState.toCurrency.code) {
        sharedPairViewModel.setPair(uiState.fromCurrency, uiState.toCurrency)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (uiState.isLoading && uiState.exchangeRates == null) {
            LoadingShimmer(Modifier.fillMaxWidth())
            LoadingShimmer(Modifier.fillMaxWidth())
        } else {
            AmountInput(
                amount = uiState.amount,
                onAmountChange = viewModel::onAmountChange,
                modifier = Modifier.fillMaxWidth()
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    CurrencyCard(
                        currency = uiState.fromCurrency,
                        onClick = { viewModel.onShowFromPicker(true) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CurrencyCard(
                        currency = uiState.toCurrency,
                        onClick = { viewModel.onShowToPicker(true) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                SwapButton(
                    onClick = viewModel::onSwapCurrencies,
                    isSwapping = uiState.isSwapping
                )
            }

            ResultCard(
                convertedAmount = uiState.convertedAmount,
                currency = uiState.toCurrency,
                isFavorite = uiState.isFavorite,
                onFavoriteClick = viewModel::onToggleFavorite,
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.exchangeRateText.isNotEmpty()) {
                Text(
                    text = uiState.exchangeRateText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (uiState.lastUpdated.isNotEmpty()) {
                Text(
                    text = "Updated ${uiState.lastUpdated}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (uiState.error != null) {
            Text(
                text = uiState.error!!,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    if (uiState.showFromPicker) {
        CurrencyPicker(
            currencies = Currency.getAll(),
            selectedCurrency = uiState.fromCurrency,
            onCurrencySelected = viewModel::onFromCurrencyChange,
            onDismiss = { viewModel.onShowFromPicker(false) }
        )
    }

    if (uiState.showToPicker) {
        CurrencyPicker(
            currencies = Currency.getAll(),
            selectedCurrency = uiState.toCurrency,
            onCurrencySelected = viewModel::onToCurrencyChange,
            onDismiss = { viewModel.onShowToPicker(false) }
        )
    }
}
