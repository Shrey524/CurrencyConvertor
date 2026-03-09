package com.shrey.currencyx.di

import android.content.Context
import com.shrey.currencyx.data.local.CurrencyDatabase
import com.shrey.currencyx.data.local.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CurrencyDatabase =
        androidx.room.Room.databaseBuilder(
            context,
            CurrencyDatabase::class.java,
            "currency_database"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideCurrencyDao(database: CurrencyDatabase): CurrencyDao =
        database.currencyDao()
}
