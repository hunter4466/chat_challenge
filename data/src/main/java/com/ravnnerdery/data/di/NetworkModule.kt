package com.ravnnerdery.data.di

import com.ravnnerdery.data.network.firestore.FirestoreDb
import com.ravnnerdery.data.network.firestore.FirestoreDbImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideFireStoreInstance(
        firestoreDbImpl: FirestoreDbImpl
    ): FirestoreDb {
        return firestoreDbImpl
    }
}