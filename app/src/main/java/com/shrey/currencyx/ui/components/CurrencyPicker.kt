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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shrey.currencyx.domain.model.Currency
import com.shrey.currencyx.ui.theme.CurrencyXColors
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPicker(
    currencies: List<Currency>,
    selectedCurrency: Currency,
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        searchQuery = ""
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CurrencyXColors.Surface,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
    ) {
        CurrencyPickerContent(
            currencies = currencies,
            selectedCurrency = selectedCurrency,
            searchQuery = searchQuery,
            onSearchChange = { searchQuery = it },
            onCurrencySelected = {
                onCurrencySelected(it)
                onDismiss()
            },
            onDismiss = onDismiss
        )
    }
}

@Composable
private fun CurrencyPickerContent(
    currencies: List<Currency>,
    selectedCurrency: Currency,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    onCurrencySelected: (Currency) -> Unit,
    onDismiss: () -> Unit
) {
    var query by remember { mutableStateOf(searchQuery) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val filteredCurrencies = remember(query, currencies) {
        if (query.isBlank()) {
            currencies
        } else {
            currencies.filter {
                it.code.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true)
            }
        }
    }

    Column(modifier = Modifier.padding(bottom = 16.dp)) {
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
                color = CurrencyXColors.TextPrimary,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(CurrencyXColors.SurfaceElevated)
                    .clickable { onDismiss() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = CurrencyXColors.TextMuted,
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
                value = query,
                onValueChange = {
                    query = it
                    onSearchChange(it)
                },
                textStyle = TextStyle(
                    color = CurrencyXColors.InputText,
                    fontSize = 16.sp
                ),
                cursorBrush = SolidColor(CurrencyXColors.Primary),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CurrencyXColors.InputBackground)
                            .border(
                                width = 1.dp,
                                color = if (query.isNotEmpty())
                                    CurrencyXColors.InputBorderFocused
                                else CurrencyXColors.InputBorder,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                            tint = CurrencyXColors.TextMuted,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.size(12.dp))
                        Box(modifier = Modifier.weight(1f)) {
                            if (query.isEmpty()) {
                                Text(
                                    text = "Search currency...",
                                    color = CurrencyXColors.InputPlaceholder,
                                    fontSize = 16.sp
                                )
                            }
                            innerTextField()
                        }

                        AnimatedVisibility(
                            visible = query.isNotEmpty(),
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut()
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .background(CurrencyXColors.SurfaceElevated)
                                    .clickable { query = "" },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Clear",
                                        tint = CurrencyXColors.TextMuted,
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
            text = if (query.isEmpty()) "All Currencies" else "${filteredCurrencies.size} Results",
            color = CurrencyXColors.TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )

        // List
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
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
                            Text(text = "🔍", fontSize = 40.sp)
                            Text(
                                text = "No currencies found",
                                color = CurrencyXColors.TextSecondary,
                                fontSize = 16.sp
                            )
                            Text(
                                text = "Try a different search term",
                                color = CurrencyXColors.TextMuted,
                                fontSize = 14.sp
                            )
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
            isSelected -> CurrencyXColors.TealGlow
            isPressed -> CurrencyXColors.InputBackground
            else -> Color.Transparent
        },
        label = "bg"
    )

    val borderColor by androidx.compose.animation.animateColorAsState(
        targetValue = if (isSelected) CurrencyXColors.TealBorder else Color.Transparent,
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
                    .background(CurrencyXColors.InputBackground),
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
                    color = CurrencyXColors.TextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = currency.name,
                    color = CurrencyXColors.TextSecondary,
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
                    .background(CurrencyXColors.Primary),
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
