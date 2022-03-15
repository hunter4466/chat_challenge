package com.ravnnerdery.data.di

import com.ravnnerdery.data.useCases.ProvidePaginatedDataSourceUseCase
import com.ravnnerdery.data.useCases.ProvidePaginatedDataSourceUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class UseCasesModule {

    @Provides
    @Singleton
    fun providePaginatedDataSourceUseCase(
        providePaginatedDataSourceUseCaseImpl: ProvidePaginatedDataSourceUseCaseImpl
    ): ProvidePaginatedDataSourceUseCase {
        return providePaginatedDataSourceUseCaseImpl
    }

}