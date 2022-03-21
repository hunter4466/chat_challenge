package com.ravnnerdery.data.useCases

import androidx.paging.PagingData
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProvidePaginatedDataSourceUseCase {
    fun execute(): Flow<PagingData<MessageEntity>>
}

class ProvidePaginatedDataSourceUseCaseImpl @Inject constructor(private val repo: MainRepository) :
    ProvidePaginatedDataSourceUseCase {
    override fun execute(): Flow<PagingData<MessageEntity>> {
        return repo.providePaginatedDataSource()
    }
}
