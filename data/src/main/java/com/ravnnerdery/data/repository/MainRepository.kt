package com.ravnnerdery.data.repository

import androidx.paging.PagingData
import com.ravnnerdery.data.database.models.MessageEntity
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun providePaginatedDataSource(): Flow<PagingData<MessageEntity>>
    suspend fun insertNewMessage(message: String)
    fun updateMessageToRead(id: String)
}