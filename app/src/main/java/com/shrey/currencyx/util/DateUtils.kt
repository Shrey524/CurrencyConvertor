package com.shrey.currencyx.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun getDateString(daysAgo: Int): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
    return formatter.format(calendar.time)
}

fun String.toEpochMillis(): Long {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    return formatter.parse(this)?.time ?: 0L
}
