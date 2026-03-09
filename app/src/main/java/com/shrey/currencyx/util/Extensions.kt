package com.shrey.currencyx.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toRelativeTimeString(): String {
    val formatter = SimpleDateFormat("MMM d, HH:mm", Locale.getDefault())
    return formatter.format(Date(this * 1000))
}
