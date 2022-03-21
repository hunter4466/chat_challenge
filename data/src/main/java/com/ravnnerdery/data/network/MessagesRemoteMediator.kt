package com.ravnnerdery.data.network

import androidx.paging.*
import com.ravnnerdery.data.database.DbWrapper
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.network.firestore.FirestoreDb
import java.util.concurrent.TimeUnit


@OptIn(ExperimentalPagingApi::class)
class MessagesRemoteMediator(
    private val dbWrapper: DbWrapper,
    private val fireStoreDb: FirestoreDb,
) : RemoteMediator<Int, MessageEntity>() {

    override suspend fun initialize(): InitializeAction {
        val cacheTimeout = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)
        return if (System.currentTimeMillis() - dbWrapper.getUpdatedDatabaseTime() >= cacheTimeout) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MessageEntity>
    ): MediatorResult {
        var isEmpty = false
        val data = when (loadType) {
            LoadType.REFRESH -> {
                fireStoreDb.getFirstMessages()
            }
            LoadType.APPEND -> {
                val res = fireStoreDb.getAppendMessages(dbWrapper.getKeyForAppend())
                if (res.isEmpty()) isEmpty = true
                res
            }
            LoadType.PREPEND -> {
                emptyList()
            }
        }

        dbWrapper.insertAllMessages(data)
        return MediatorResult.Success(endOfPaginationReached = isEmpty)
    }
}