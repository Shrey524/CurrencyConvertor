package com.shrey.currencyx.util

import android.util.Log

/**
 * Single app log tag. Use [e] only for errors and cache fallback.
 */
object LogUtil {
    private const val TAG = "CurrencyX"

    fun e(message: String, throwable: Throwable? = null) {
        if (throwable != null) Log.e(TAG, message, throwable) else Log.e(TAG, message)
    }
}
