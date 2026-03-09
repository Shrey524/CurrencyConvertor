package com.shrey.currencyx.ui.charts

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shrey.currencyx.domain.model.ChartPeriod
import com.shrey.currencyx.domain.model.ChartPoint
import com.shrey.currencyx.domain.model.Currency
import com.shrey.currencyx.ui.SharedPairViewModel
import com.shrey.currencyx.ui.components.CurrencyPicker
import com.shrey.currencyx.ui.theme.CurrencyXColors
import com.shrey.currencyx.ui.theme.CurrencyXDimens
import com.shrey.currencyx.ui.theme.CurrencyXGradients

@Composable
fun ChartsScreen(
    viewModel: ChartsViewModel = hiltViewModel(),
    onNavigateToConvert: () -> Unit = {}
) {
    val sharedPairViewModel: SharedPairViewModel = hiltViewModel()
    val sharedPair by sharedPairViewModel.pair.collectAsStateWithLifecycle()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle(initialValue = ChartUiState())
    val scrollState = rememberScrollState()

    LaunchedEffect(sharedPair.from.code, sharedPair.to.code) {
        val desiredFrom = if (Currency.isFrankfurterSupported(sharedPair.from.code)) {
            sharedPair.from.code
        } else {
            "USD"
        }
        val desiredTo = if (Currency.isFrankfurterSupported(sharedPair.to.code)) {
            sharedPair.to.code
        } else {
            "INR"
        }

        if (desiredFrom != uiState.fromCurrency || desiredTo != uiState.toCurrency) {
            viewModel.selectFromCurrency(desiredFrom)
            viewModel.selectToCurrency(desiredTo)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CurrencyXColors.Background)
            .verticalScroll(scrollState)
            .padding(horizontal = CurrencyXDimens.PaddingScreen)
            .padding(top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val converterFromSupported = Currency.isFrankfurterSupported(sharedPair.from.code)
        val converterToSupported = Currency.isFrankfurterSupported(sharedPair.to.code)

        if (!converterFromSupported || !converterToSupported) {
            Text(
                text = "Charts use ECB data and only support a subset of currencies. " +
                    "The current converter pair ${sharedPair.from.code}/${sharedPair.to.code} " +
                    "cannot be charted; showing the closest supported pair instead.",
                color = CurrencyXColors.TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }
        CurrencyPairSelector(
            fromCurrency = uiState.fromCurrency,
            toCurrency = uiState.toCurrency,
            onSwap = { viewModel.swapCurrencies() },
            onFromClick = { viewModel.onShowFromPicker(true) },
            onToClick = { viewModel.onShowToPicker(true) }
        )

        PeriodSelector(
            selectedPeriod = uiState.selectedPeriod,
            onPeriodSelected = { viewModel.selectPeriod(it) }
        )

        ChartCard(
            fromCurrency = uiState.fromCurrency,
            toCurrency = uiState.toCurrency,
            currentRate = uiState.currentRate,
            changePercent = uiState.changePercent,
            isPositive = uiState.isPositive,
            chartData = uiState.chartData,
            isLoading = uiState.isLoading
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = "💱",
                label = "Current Rate",
                value = formatRate(uiState.currentRate),
                accentColor = CurrencyXColors.ChartPositive
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = if (uiState.isPositive) "📈" else "📉",
                label = "Change",
                value = "${if (uiState.isPositive) "+" else ""}${String.format("%.2f", uiState.changePercent)}%",
                accentColor = if (uiState.isPositive) CurrencyXColors.ChartPositive else CurrencyXColors.ChartNegative,
                valueColor = if (uiState.isPositive) CurrencyXColors.ChartPositive else CurrencyXColors.ChartNegative
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                icon = "📈",
                label = "High",
                value = formatRate(uiState.highRate),
                accentColor = CurrencyXColors.ChartPositive
            )
            StatCard(
                modifier = Modifier.weight(1f),
                icon = "📉",
                label = "Low",
                value = formatRate(uiState.lowRate),
                accentColor = CurrencyXColors.Warning
            )
        }

        AverageCard(
            period = uiState.selectedPeriod,
            average = uiState.averageRate,
            toCurrency = uiState.toCurrency,
            onRefresh = { viewModel.refresh() }
        )
    }

    if (uiState.showFromPicker) {
        CurrencyPicker(
            currencies = Currency.getFrankfurterSupported(),
            selectedCurrency = Currency.getDefault(uiState.fromCurrency),
            onCurrencySelected = { viewModel.selectFromCurrency(it.code) },
            onDismiss = { viewModel.onShowFromPicker(false) }
        )
    }

    if (uiState.showToPicker) {
        CurrencyPicker(
            currencies = Currency.getFrankfurterSupported(),
            selectedCurrency = Currency.getDefault(uiState.toCurrency),
            onCurrencySelected = { viewModel.selectToCurrency(it.code) },
            onDismiss = { viewModel.onShowToPicker(false) }
        )
    }
}

@Composable
private fun CurrencyPairSelector(
    fromCurrency: String,
    toCurrency: String,
    onSwap: () -> Unit,
    onFromClick: () -> Unit,
    onToClick: () -> Unit
) {
    val fromData = Currency.getByCode(fromCurrency)
    val toData = Currency.getByCode(toCurrency)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CurrencyXDimens.RadiusXl),
        colors = CardDefaults.cardColors(containerColor = CurrencyXColors.Surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CurrencyChip(
                flag = fromData?.flagEmoji ?: "🏳️",
                code = fromCurrency,
                name = fromData?.name ?: "",
                onClick = onFromClick
            )

            SwapButton(onClick = onSwap)

            CurrencyChip(
                flag = toData?.flagEmoji ?: "🏳️",
                code = toCurrency,
                name = toData?.name ?: "",
                onClick = onToClick,
                isReversed = true
            )
        }
    }
}

@Composable
private fun CurrencyChip(
    flag: String,
    code: String,
    name: String,
    onClick: () -> Unit,
    isReversed: Boolean = false
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalArrangement = if (isReversed) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!isReversed) {
            Text(text = flag, fontSize = 28.sp)
            Spacer(modifier = Modifier.size(8.dp))
        }

        Column(
            horizontalAlignment = if (isReversed) Alignment.End else Alignment.Start
        ) {
            Text(
                text = code,
                color = CurrencyXColors.TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = name,
                color = CurrencyXColors.TextSecondary,
                fontSize = 11.sp
            )
        }

        if (isReversed) {
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = flag, fontSize = 28.sp)
        }
    }
}

@Composable
private fun SwapButton(onClick: () -> Unit) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = spring(dampingRatio = 0.6f),
        label = "scale"
    )

    Box(
        modifier = Modifier
            .size(CurrencyXDimens.FlagContainerSize)
            .scale(scale)
            .clip(CircleShape)
            .background(CurrencyXColors.TealGlow)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                isPressed = true
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Rounded.SwapHoriz,
            contentDescription = "Swap",
            tint = CurrencyXColors.Primary,
            modifier = Modifier.size(24.dp)
        )
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}

@Composable
private fun PeriodSelector(
    selectedPeriod: ChartPeriod,
    onPeriodSelected: (ChartPeriod) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ChartPeriod.entries.forEach { period ->
            val isSelected = period == selectedPeriod
            val backgroundColor by animateColorAsState(
                targetValue = if (isSelected) CurrencyXColors.Primary else CurrencyXColors.Surface,
                label = "bg"
            )
            val textColor by animateColorAsState(
                targetValue = if (isSelected) CurrencyXColors.OnPrimary else CurrencyXColors.TextSecondary,
                label = "text"
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(CurrencyXDimens.ButtonHeightSmall)
                    .clip(RoundedCornerShape(CurrencyXDimens.RadiusMd))
                    .background(backgroundColor)
                    .clickable { onPeriodSelected(period) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = period.label,
                    color = textColor,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun ChartCard(
    fromCurrency: String,
    toCurrency: String,
    currentRate: Double,
    changePercent: Double,
    isPositive: Boolean,
    chartData: List<ChartPoint>,
    isLoading: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CurrencyXDimens.RadiusXl),
        colors = CardDefaults.cardColors(containerColor = CurrencyXColors.Surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "1 $fromCurrency equals",
                        color = CurrencyXColors.TextMuted,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatRate(currentRate),
                        color = CurrencyXColors.TextPrimary,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = Currency.getByCode(toCurrency)?.name ?: toCurrency,
                        color = CurrencyXColors.TextSecondary,
                        fontSize = 14.sp
                    )
                }

                Surface(
                    shape = RoundedCornerShape(CurrencyXDimens.RadiusLg),
                    color = if (isPositive) CurrencyXColors.TealGlow else CurrencyXColors.ErrorMuted
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (isPositive) "📈" else "📉",
                            fontSize = 14.sp
                        )
                        Text(
                            text = "${if (isPositive) "+" else ""}${String.format("%.2f", changePercent)}%",
                            color = if (isPositive) CurrencyXColors.ChartPositive else CurrencyXColors.ChartNegative,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = CurrencyXColors.Primary)
                }
            } else {
                DynamicYAxisChart(
                    data = chartData,
                    isPositive = isPositive,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }
        }
    }
}

@Composable
private fun DynamicYAxisChart(
    data: List<ChartPoint>,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val lineColor = if (isPositive) CurrencyXColors.ChartPositive else CurrencyXColors.ChartNegative
    val gradientColors = if (isPositive) {
        listOf(CurrencyXColors.ChartPositive.copy(alpha = 0.3f), CurrencyXColors.ChartPositive.copy(alpha = 0.02f))
    } else {
        listOf(CurrencyXColors.ChartNegative.copy(alpha = 0.3f), CurrencyXColors.ChartNegative.copy(alpha = 0.02f))
    }

    Canvas(modifier = modifier) {
        if (data.size < 2) return@Canvas

        val rates = data.map { it.rate.toFloat() }
        val dataMin = rates.minOrNull() ?: 0f
        val dataMax = rates.maxOrNull() ?: 0f

        val rangePadding = (dataMax - dataMin) * 0.1f
        val minRate = dataMin - rangePadding
        val maxRate = dataMax + rangePadding
        val range = maxRate - minRate

        val leftPadding = 50.dp.toPx()
        val rightPadding = 12.dp.toPx()
        val topPadding = 12.dp.toPx()
        val bottomPadding = 24.dp.toPx()

        val chartWidth = size.width - leftPadding - rightPadding
        val chartHeight = size.height - topPadding - bottomPadding

        fun getX(index: Int): Float = leftPadding + (index.toFloat() / (data.size - 1).coerceAtLeast(1)) * chartWidth
        fun getY(rate: Float): Float = topPadding + ((maxRate - rate) / range) * chartHeight

        val yAxisSteps = 4
        for (i in 0..yAxisSteps) {
            val value = maxRate - (i.toFloat() / yAxisSteps) * range
            val y = topPadding + (i.toFloat() / yAxisSteps) * chartHeight

            drawLine(
                color = CurrencyXColors.ChartGrid.copy(alpha = 0.4f),
                start = Offset(leftPadding, y),
                end = Offset(size.width - rightPadding, y),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f))
            )

            val labelText = String.format("%.1f", value)
            val textLayoutResult = textMeasurer.measure(
                text = labelText,
                style = TextStyle(
                    fontSize = 10.sp,
                    color = CurrencyXColors.ChartLabel
                )
            )
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = leftPadding - textLayoutResult.size.width - 8.dp.toPx(),
                    y = y - textLayoutResult.size.height / 2
                )
            )
        }

        val linePath = Path().apply {
            data.forEachIndexed { index, point ->
                val x = getX(index)
                val y = getY(point.rate.toFloat())
                if (index == 0) moveTo(x, y) else lineTo(x, y)
            }
        }

        val areaPath = Path().apply {
            addPath(linePath)
            lineTo(getX(data.size - 1), chartHeight + topPadding)
            lineTo(leftPadding, chartHeight + topPadding)
            close()
        }

        drawPath(
            path = areaPath,
            brush = Brush.verticalGradient(
                colors = gradientColors,
                startY = topPadding,
                endY = chartHeight + topPadding
            )
        )

        drawPath(
            path = linePath,
            color = lineColor,
            style = Stroke(
                width = 2.5.dp.toPx(),
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        val lastX = getX(data.size - 1)
        val lastY = getY(data.last().rate.toFloat())

        drawCircle(
            color = lineColor.copy(alpha = 0.3f),
            radius = 10.dp.toPx(),
            center = Offset(lastX, lastY)
        )
        drawCircle(
            color = lineColor,
            radius = 5.dp.toPx(),
            center = Offset(lastX, lastY)
        )

        val xLabels = listOf(0, data.size / 3, 2 * data.size / 3, data.size - 1)
        xLabels.forEach { index ->
            if (index in data.indices) {
                val label = data[index].date
                val textLayoutResult = textMeasurer.measure(
                    text = label,
                    style = TextStyle(
                        fontSize = 10.sp,
                        color = CurrencyXColors.ChartLabel
                    )
                )
                drawText(
                    textLayoutResult = textLayoutResult,
                    topLeft = Offset(
                        x = getX(index) - textLayoutResult.size.width / 2,
                        y = size.height - textLayoutResult.size.height
                    )
                )
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: String,
    label: String,
    value: String,
    accentColor: Color,
    valueColor: Color = Color.White
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(CurrencyXDimens.RadiusLg),
        colors = CardDefaults.cardColors(containerColor = CurrencyXColors.Surface)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = icon, fontSize = 20.sp)
            }

            Column {
                Text(
                    text = label,
                    color = CurrencyXColors.TextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = value,
                    color = valueColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun AverageCard(
    period: ChartPeriod,
    average: Double,
    toCurrency: String,
    onRefresh: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CurrencyXDimens.RadiusXl),
        colors = CardDefaults.cardColors(
            containerColor = CurrencyXColors.TealMuted
        ),
        border = BorderStroke(
            1.dp,
            CurrencyXGradients.BorderPrimary
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(CurrencyXColors.TealGlow),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "📊", fontSize = 24.sp)
                }

                Column {
                    Text(
                        text = "${period.label} Average",
                        color = CurrencyXColors.TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = formatRate(average),
                            color = CurrencyXColors.TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            text = toCurrency,
                            color = CurrencyXColors.Primary,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            IconButton(
                onClick = onRefresh,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(CurrencyXColors.TealGlow)
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh",
                    tint = CurrencyXColors.Primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun formatRate(rate: Double): String {
    return when {
        rate >= 100 -> String.format("%.2f", rate)
        rate >= 1 -> String.format("%.4f", rate)
        else -> String.format("%.6f", rate)
    }
}
