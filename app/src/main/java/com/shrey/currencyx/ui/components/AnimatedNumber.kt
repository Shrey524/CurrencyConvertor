package com.shrey.currencyx.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnimatedNumber(
    targetValue: Double,
    modifier: Modifier = Modifier
) {
    val animated by animateFloatAsState(
        targetValue = targetValue.toFloat(),
        label = "amount"
    )
    val formatter = NumberFormat.getNumberInstance(Locale.US).apply {
        minimumFractionDigits = 2
        maximumFractionDigits = 2
    }
    Text(
        text = formatter.format(animated.toDouble()),
        style = MaterialTheme.typography.displayLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = modifier
    )
}
