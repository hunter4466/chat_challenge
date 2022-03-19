package com.ravnnerdery.data.repository

import androidx.paging.*
import com.ravnnerdery.data.database.DatabaseDao
import com.ravnnerdery.data.database.DbWrapper
import com.ravnnerdery.data.database.MessagesDatabase
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.network.MessagesPagingDataSource
import com.ravnnerdery.data.network.MessagesRemoteMediator
import com.ravnnerdery.data.network.firestore.FirestoreDb
import com.ravnnerdery.data.utils.randKeyGen
import com.ravnnerdery.domain.models.Message
import com.ravnnerdery.domain.repository.MainRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MainRepositoryImpl
@Inject constructor(
    private val databaseDao: DatabaseDao,
    private val dbWrapper: DbWrapper,
    private val database: MessagesDatabase,
    private val fireStoreDb: FirestoreDb,
) : MainRepository {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    @OptIn(ExperimentalPagingApi::class)
    override fun providePaginatedDataSource(): Flow<PagingData<Message>> = Pager(
        config = PagingConfig(
            pageSize = 25,
            enablePlaceholders = false),
        remoteMediator = MessagesRemoteMediator(dbWrapper, database, fireStoreDb),
        pagingSourceFactory = {
            MessagesPagingDataSource(dbWrapper)
        },
        initialKey = "0"
    ).flow.cachedIn(uiScope)


    override fun insertNewMessage(message: String) {
        uiScope.launch(Dispatchers.IO){
            databaseDao.insertMessage(
                MessageEntity(
                    messageId = randKeyGen(),
                    message = message,
                    time = System.currentTimeMillis(),
                    read = false,
                    userId = "mario_chois",
                    image = "www.placeHolderUrl.com",
                )
            )
        }
    }
}