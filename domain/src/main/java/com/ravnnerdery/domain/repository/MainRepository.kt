package com.ravnnerdery.domain.repository

import androidx.paging.PagingData
import com.ravnnerdery.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun providePaginatedDataSource(): Flow<PagingData<Message>>
    fun insertNewMessage(message: String)
}