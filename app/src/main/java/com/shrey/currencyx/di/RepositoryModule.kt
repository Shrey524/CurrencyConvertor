package com.shrey.currencyx.di

import com.shrey.currencyx.data.repository.ChartRepositoryImpl
import com.shrey.currencyx.data.repository.CurrencyRepositoryImpl
import com.shrey.currencyx.domain.repository.ChartRepository
import com.shrey.currencyx.domain.repository.CurrencyRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCurrencyRepository(impl: CurrencyRepositoryImpl): CurrencyRepository

    @Binds
    @Singleton
    abstract fun bindChartRepository(impl: ChartRepositoryImpl): ChartRepository
}
