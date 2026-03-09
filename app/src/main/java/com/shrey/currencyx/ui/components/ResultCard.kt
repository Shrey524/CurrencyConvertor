package com.shrey.currencyx.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.shrey.currencyx.domain.model.Currency
import com.shrey.currencyx.ui.theme.Emerald600
import com.shrey.currencyx.ui.theme.Emerald700
import com.shrey.currencyx.ui.theme.Slate100

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
        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                tint = Slate100
            )
        }
        Column(modifier = Modifier.padding(top = 8.dp)) {
            Text(
                "Converted Amount",
                style = MaterialTheme.typography.labelMedium,
                color = Slate100.copy(alpha = 0.9f)
            )
            AnimatedNumber(
                targetValue = convertedAmount,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            Text(
                text = currency.code,
                style = MaterialTheme.typography.titleLarge,
                color = Slate100
            )
        }
    }
}
