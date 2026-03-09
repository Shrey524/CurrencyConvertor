package com.shrey.currencyx.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.shrey.currencyx.ui.theme.Emerald500

@Composable
fun SwapButton(
    onClick: () -> Unit,
    isSwapping: Boolean,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(if (isSwapping) 180f else 0f)
    val haptic = LocalHapticFeedback.current

    Box(
        modifier = modifier
            .size(56.dp)
            .rotate(rotation)
            .background(Emerald500, CircleShape)
            .clickable(
                role = Role.Button,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onClick()
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.SwapVert,
            contentDescription = "Swap currencies",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}
