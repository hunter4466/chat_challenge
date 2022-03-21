package com.ravnnerdery.data.di

import com.ravnnerdery.data.repository.MainRepositoryImpl
import com.ravnnerdery.data.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepositoryImpl(
        mainRepositoryImpl: MainRepositoryImpl
    ): MainRepository {
        return mainRepositoryImpl
    }

}