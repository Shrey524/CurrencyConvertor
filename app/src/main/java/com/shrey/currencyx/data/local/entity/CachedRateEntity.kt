package com.shrey.currencyx.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cached_rates")
data class CachedRateEntity(
    @PrimaryKey val baseCurrency: String,
    val ratesJson: String,
    val lastUpdated: Long
)
