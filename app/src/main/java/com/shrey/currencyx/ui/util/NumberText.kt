package com.shrey.currencyx.ui.util

import kotlin.math.abs
import kotlin.math.floor

private val UNITS = arrayOf(
    "",
    "thousand",
    "million",
    "billion",
    "trillion",
    "quadrillion",
    "quintillion"
)

/**
 * Returns a compact short-scale text like "128.1 billion" or "500".
 * For 0 -> "zero". Negative values get a "minus" prefix.
 */
fun Double.toShortScaleText(): String {
    if (!this.isFinite()) return ""
    if (this == 0.0) return "zero"

    val sign = if (this < 0) "minus " else ""
    var value = abs(this)
    var unitIndex = 0

    while (value >= 1000 && unitIndex < UNITS.lastIndex) {
        value /= 1000
        unitIndex++
    }

    // Round to 1 decimal place for nicer text
    val rounded = (value * 10).let { floor(it + 0.5) / 10.0 }
    val numberPart = if (rounded % 1.0 == 0.0) rounded.toInt().toString() else rounded.toString()

    return when (unitIndex) {
        0 -> sign + numberPart
        else -> sign + "$numberPart ${UNITS[unitIndex]}"
    }
}
