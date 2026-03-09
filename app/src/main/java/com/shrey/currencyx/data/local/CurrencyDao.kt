package com.shrey.currencyx.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shrey.currencyx.data.local.entity.CachedRateEntity

@Dao
interface CurrencyDao {
    @Query("SELECT * FROM cached_rates WHERE baseCurrency = :base")
    suspend fun getCachedRates(base: String): CachedRateEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCachedRates(rates: CachedRateEntity)
}
