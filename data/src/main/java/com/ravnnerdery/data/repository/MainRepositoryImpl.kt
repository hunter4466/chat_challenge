package com.ravnnerdery.data.repository

import android.util.Log
import androidx.paging.PagingData
import com.ravnnerdery.domain.models.Message
import com.ravnnerdery.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
): MainRepository {
    override fun providePaginatedDataSource(): Flow<PagingData<Message>> {
        TODO()
    }
}