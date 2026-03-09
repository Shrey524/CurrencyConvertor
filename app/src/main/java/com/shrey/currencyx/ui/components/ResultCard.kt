package com.shrey.currencyx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.shrey.currencyx.domain.model.Currency
import com.shrey.currencyx.ui.theme.Emerald600
import com.shrey.currencyx.ui.theme.Emerald700
import com.shrey.currencyx.ui.theme.Slate100
import com.shrey.currencyx.ui.util.toShortScaleText

@Composable
fun ResultCard(
    convertedAmount: Double,
    currency: Currency,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(Emerald600, Emerald700)
                )
            )
            .padding(24.dp)
    ) {
        Column(modifier = Modifier) {
            Text(
                "Converted Amount",
                style = MaterialTheme.typography.labelMedium,
                color = Slate100.copy(alpha = 0.9f)
            )
            AnimatedNumber(
                targetValue = convertedAmount,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            // Human-readable short-scale text (e.g., "128.1 billion")
            Text(
                text = convertedAmount.toShortScaleText(),
                style = MaterialTheme.typography.bodySmall,
                color = Slate100.copy(alpha = 0.85f)
            )
            Text(
                text = currency.code,
                style = MaterialTheme.typography.titleLarge,
                color = Slate100
            )
        }
    }
}
