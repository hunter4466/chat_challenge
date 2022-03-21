package com.ravnnerdery.data.repository

import androidx.paging.*
import com.ravnnerdery.data.database.DbWrapper
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.network.MessagesRemoteMediator
import com.ravnnerdery.data.network.firestore.FirestoreDb
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl
@Inject constructor(
    private val dbWrapper: DbWrapper,
    private val fireStoreDb: FirestoreDb,
) : MainRepository {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    @OptIn(ExperimentalPagingApi::class)
    override fun providePaginatedDataSource(): Flow<PagingData<MessageEntity>> = Pager(
        config = PagingConfig(
            pageSize = 5,
            enablePlaceholders = false,
        ),
        remoteMediator = MessagesRemoteMediator(dbWrapper, fireStoreDb),
        pagingSourceFactory = {
            dbWrapper.getPaginatedMessages()
        },
    ).flow.cachedIn(uiScope)


    override suspend fun insertNewMessage(message: String) {
        fireStoreDb.addMessage(message)
    }

    override fun updateMessageToRead(id: String) {
        fireStoreDb.updateMessageToRead(id)
    }
}