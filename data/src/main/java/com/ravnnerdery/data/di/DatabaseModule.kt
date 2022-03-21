package com.ravnnerdery.data.di

import android.content.Context
import androidx.room.Room
import com.ravnnerdery.data.database.DatabaseDao
import com.ravnnerdery.data.database.DbWrapper
import com.ravnnerdery.data.database.DbWrapperImpl
import com.ravnnerdery.data.database.MessagesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val CHAT_CHALLENGE_DATABASE = "Chat_challenge_db"

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabaseDao(articlesDatabase: MessagesDatabase): DatabaseDao {
        return articlesDatabase.databaseDao()
    }

    @Provides
    @Singleton
    fun provideMessagesDatabase(@ApplicationContext appContext: Context): MessagesDatabase {
        return Room.databaseBuilder(
            appContext,
            MessagesDatabase::class.java,
            CHAT_CHALLENGE_DATABASE
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideDbWrapper(
        dbWrapperImpl: DbWrapperImpl
    ): DbWrapper {
        return dbWrapperImpl
    }

}