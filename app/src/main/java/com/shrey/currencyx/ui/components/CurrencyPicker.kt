package com.shrey.currencyx.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrey.currencyx.domain.model.Currency
import com.shrey.currencyx.ui.theme.Emerald500
import com.shrey.currencyx.ui.theme.Slate400
import com.shrey.currencyx.ui.theme.Slate800
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

// Local copies of slate/emerald shades to match charts style
private val SlateBackground = Color(0xFF0F172A)
private val SlateCard = Color(0xFF1E293B)
private val SlateCardLight = Color(0xFF334155)
private val SlateBorder = Color(0xFF475569)
private val SlateText = Color(0xFF94A3B8)
private val SlateTextMuted = Color(0xFF64748B)
private val EmeraldPrimary = Color(0xFF10B981)
private val EmeraldGlow = Color(0x3310B981)

@Composable
fun CurrencyPicker(
    currencies: List<Currency>,
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    CurrencyPickerBottomSheet(
        currencies = currencies,
        selectedCurrency = selectedCurrency,
        onCurrencySelected = onCurrencySelected,
        onDismiss = onDismiss
    )
}

@Composable
private fun CurrencyPickerBottomSheet(
    currencies: List<Currency>,
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val filteredCurrencies = remember(searchQuery, currencies) {
        if (searchQuery.isBlank()) {
            currencies
        } else {
            currencies.filter {
                it.code.contains(searchQuery, ignoreCase = true) ||
                    it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    var sheetOffset by remember { mutableFloatStateOf(0f) }
    val sheetHeight = 600.dp
    val dismissThreshold = 150f
    val density = LocalDensity.current
    val sheetHeightPx = with(density) { sheetHeight.toPx() }

    // Simple spring animation for snapping back after drag
    val animatedOffset by animateFloatAsState(
        targetValue = sheetOffset,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "sheetOffset"
    )

    LaunchedEffect(Unit) {
        // Reset state when shown
        searchQuery = ""
        sheetOffset = 0f
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f * (1 - (animatedOffset / sheetHeightPx).coerceIn(0f, 1f))))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onDismiss
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeight)
                .offset { IntOffset(0, animatedOffset.roundToInt()) }
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(SlateBackground)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = { }
                )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Drag handle area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) {
                            detectVerticalDragGestures(
                                onDragEnd = {
                                    if (sheetOffset > dismissThreshold) {
                                        onDismiss()
                                    } else {
                                        coroutineScope.launch { sheetOffset = 0f }
                                    }
                                },
                                onDragCancel = {
                                    coroutineScope.launch { sheetOffset = 0f }
                                },
                                onVerticalDrag = { _, dragAmount ->
                                    sheetOffset = (sheetOffset + dragAmount).coerceAtLeast(0f)
                                }
                            )
                        }
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 40.dp, height = 4.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(SlateCardLight)
                    )
                }

                // Header
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Currency",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(SlateCard)
                            .clickable { onDismiss() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "Close",
                            tint = SlateText,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Search bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 16.dp)
                ) {
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp
                        ),
                        cursorBrush = SolidColor(EmeraldPrimary),
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(SlateCard)
                                    .border(
                                        width = 1.dp,
                                        color = if (searchQuery.isNotEmpty()) EmeraldPrimary.copy(alpha = 0.5f) else SlateBorder.copy(
                                            alpha = 0.3f
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = null,
                                    tint = SlateTextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.size(12.dp))
                                Box(modifier = Modifier.weight(1f)) {
                                    if (searchQuery.isEmpty()) {
                                        Text(
                                            text = "Search currency...",
                                            color = SlateTextMuted,
                                            fontSize = 16.sp
                                        )
                                    }
                                    innerTextField()
                                }

                                AnimatedVisibility(
                                    visible = searchQuery.isNotEmpty(),
                                    enter = fadeIn() + scaleIn(),
                                    exit = fadeOut() + scaleOut()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                            .background(SlateCardLight)
                                            .clickable { searchQuery = "" },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Close,
                                            contentDescription = "Clear",
                                            tint = SlateText,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                            }
                        }
                    )
                }

                // Section header
                Text(
                    text = if (searchQuery.isEmpty()) "All Currencies" else "${filteredCurrencies.size} Results",
                    color = SlateText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )

                // List
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    contentPadding = PaddingValues(bottom = 32.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(
                        items = filteredCurrencies,
                        key = { it.code }
                    ) { currency ->
                        CurrencyListItem(
                            currency = currency,
                            isSelected = currency.code == selectedCurrency.code,
                            onClick = {
                                onCurrencySelected(currency)
                                onDismiss()
                            }
                        )
                    }

                    if (filteredCurrencies.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "🔍",
                                        fontSize = 40.sp
                                    )
                                    Text(
                                        text = "No currencies found",
                                        color = SlateText,
                                        fontSize = 16.sp
                                    )
                                    Text(
                                        text = "Try a different search term",
                                        color = SlateTextMuted,
                                        fontSize = 14.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrencyListItem(
    currency: Currency,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "scale"
    )

    val backgroundColor by androidx.compose.animation.animateColorAsState(
        targetValue = when {
            isSelected -> EmeraldGlow
            isPressed -> Slate800
            else -> Color.Transparent
        },
        label = "bg"
    )

    val borderColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) EmeraldPrimary.copy(alpha = 0.5f) else Color.Transparent,
        label = "border"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Slate800),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = currency.flagEmoji,
                    fontSize = 24.sp
                )
            }

            Column {
                Text(
                    text = currency.code,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = currency.name,
                    color = SlateText,
                    fontSize = 13.sp
                )
            }
        }

        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn() + scaleIn(),
            exit = fadeOut() + scaleOut()
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(EmeraldPrimary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}
