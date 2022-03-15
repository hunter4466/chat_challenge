package com.ravnnerdery.data.useCases

import androidx.paging.PagingData
import com.ravnnerdery.domain.models.Message
import com.ravnnerdery.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ProvidePaginatedDataSourceUseCase {
    fun execute(): Flow<PagingData<Message>>
}

class ProvidePaginatedDataSourceUseCaseImpl @Inject constructor( private val repo: MainRepository ):
    ProvidePaginatedDataSourceUseCase {
    override fun execute(): Flow<PagingData<Message>> {
        return repo.providePaginatedDataSource()
    }
}