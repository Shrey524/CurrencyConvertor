package com.shrey.currencyx.ui.chart.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.shrey.currencyx.domain.model.ChartStats
import com.shrey.currencyx.ui.theme.Emerald500

@Composable
fun StatsGrid(
    stats: ChartStats,
    toCurrencyCode: String,
    modifier: Modifier = Modifier
) {
    val changeColor = if (stats.isPositive) Emerald500 else Color(0xFFEF4444)
    val changePrefix = if (stats.isPositive) "+" else ""

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedStatsCard(
                title = "Current Rate",
                value = "",
                icon = Icons.Default.CurrencyExchange,
                index = 0,
                modifier = Modifier.weight(1f),
                animatedValue = stats.currentRate,
                valueFormat = "%.4f"
            )
            AnimatedStatsCard(
                title = "Change",
                value = "$changePrefix${String.format("%.2f", stats.changePercent)}%",
                icon = if (stats.isPositive) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                valueColor = changeColor,
                index = 1,
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedStatsCard(
                title = "High",
                value = "",
                icon = Icons.Default.ArrowUpward,
                index = 2,
                modifier = Modifier.weight(1f),
                animatedValue = stats.highRate,
                valueFormat = "%.4f"
            )
            AnimatedStatsCard(
                title = "Low",
                value = "",
                icon = Icons.Default.ArrowDownward,
                index = 3,
                modifier = Modifier.weight(1f),
                animatedValue = stats.lowRate,
                valueFormat = "%.4f"
            )
        }
        AnimatedStatsCard(
            title = "Average",
            value = String.format("%.4f %s", stats.averageRate, toCurrencyCode),
            icon = Icons.Default.Analytics,
            index = 4,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
