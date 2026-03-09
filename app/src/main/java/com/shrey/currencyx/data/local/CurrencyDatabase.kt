package com.shrey.currencyx.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.shrey.currencyx.data.local.entity.CachedRateEntity

@Database(
    entities = [CachedRateEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CurrencyDatabase : RoomDatabase() {
    abstract fun currencyDao(): CurrencyDao
}
