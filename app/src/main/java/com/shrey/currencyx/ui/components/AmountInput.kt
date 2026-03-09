package com.shrey.currencyx.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrey.currencyx.ui.theme.Emerald500
import com.shrey.currencyx.ui.theme.Slate400
import com.shrey.currencyx.ui.theme.Slate800

@Composable
fun AmountInput(
    amount: String,
    onAmountChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusState = remember { mutableStateOf(false) }
    val borderAlpha by animateFloatAsState(if (focusState.value) 1f else 0.5f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Slate800)
            .border(
                width = (1 + borderAlpha).dp,
                color = Emerald500.copy(alpha = borderAlpha * 0.8f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
            .onFocusChanged { focusState.value = it.isFocused }
    ) {
        if (amount.isEmpty() && !focusState.value) {
            Text(
                "Amount",
                style = MaterialTheme.typography.bodyLarge,
                color = Slate400,
                modifier = Modifier.align(Alignment.CenterStart)
            )
        }
        BasicTextField(
            value = amount,
            onValueChange = { new ->
                if (new.isEmpty() || new.matches(Regex("^\\d*\\.?\\d*$"))) onAmountChange(new)
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart),
            textStyle = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 32.sp,
                color = MaterialTheme.colorScheme.onSurface
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true
        )
    }
}
