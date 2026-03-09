package com.shrey.currencyx.ui.chart.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.compose.component.lineComponent
import com.patrykandpatrick.vico.compose.component.shape.shader.verticalGradient
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.entryOf
import com.shrey.currencyx.domain.model.ChartData
import com.shrey.currencyx.ui.theme.Emerald500

@Composable
fun ExchangeRateChart(
    chartData: ChartData?,
    isPositive: Boolean,
    modifier: Modifier = Modifier
) {
    if (chartData == null) {
        ChartPlaceholder("No data available", modifier)
        return
    }

    val validPoints = remember(chartData) {
        chartData.points.filter { it.rate.isFinite() && it.rate > 0 }
    }

    if (validPoints.size < 2) {
        ChartPlaceholder("Insufficient data", modifier)
        return
    }

    val chartColor = if (isPositive) Emerald500 else Color(0xFFEF4444)

    val chartEntryModel = remember(validPoints) {
        entryModelOf(
            validPoints.mapIndexed { index, point ->
                entryOf(index.toFloat(), point.rate.toFloat())
            }
        )
    }

    val lineSpec = remember(chartColor) {
        listOf(
            lineSpec(
                lineColor = chartColor,
                lineBackgroundShader = verticalGradient(
                    arrayOf(chartColor.copy(alpha = 0.4f), chartColor.copy(alpha = 0f))
                )
            )
        )
    }

    Chart(
        chart = lineChart(lines = lineSpec),
        model = chartEntryModel,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        startAxis = rememberStartAxis(
            label = textComponent(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textSize = 10.sp
            ),
            tick = null,
            guideline = lineComponent(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = 1.dp
            )
        ),
        bottomAxis = rememberBottomAxis(
            label = textComponent(
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textSize = 10.sp
            ),
            tick = null,
            guideline = null,
            valueFormatter = { value, _ ->
                val index = value.toInt()
                if (index in validPoints.indices) validPoints[index].date.substring(5) else ""
            }
        )
    )
}

@Composable
private fun ChartPlaceholder(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
