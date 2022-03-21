package com.ravnnerdery.data.di

import com.ravnnerdery.data.useCases.*
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

    @Provides
    @Singleton
    fun insertNewMessageUseCase(
        insertNewMessageUseCaseImpl: InsertNewMessageUseCaseImpl
    ): InsertNewMessageUseCase {
        return insertNewMessageUseCaseImpl
    }

    @Provides
    @Singleton
    fun provideUpdateMessageToReadUseCase(
        provideUpdateMessageToReadUseCaseImpl: ProvideUpdateMessageToReadUseCaseImpl
    ): ProvideUpdateMessageToReadUseCase {
        return provideUpdateMessageToReadUseCaseImpl
    }

}