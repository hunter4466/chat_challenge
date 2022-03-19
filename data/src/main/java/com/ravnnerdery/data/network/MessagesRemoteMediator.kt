package com.ravnnerdery.data.network

import android.util.Log
import androidx.paging.*
import com.ravnnerdery.data.database.DbWrapper
import com.ravnnerdery.data.database.MessagesDatabase
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.network.firestore.FirestoreDb
import com.ravnnerdery.domain.models.Message
import kotlinx.coroutines.delay
import javax.inject.Inject


@OptIn(ExperimentalPagingApi::class)
class MessagesRemoteMediator(
    private val dbWrapper: DbWrapper,
    private val database: MessagesDatabase,
    private val fireStoreDb: FirestoreDb,
) : RemoteMediator<String, Message>() {

    override suspend fun initialize(): InitializeAction {
        Log.wtf("MARIOCH", "CALLED INITIALIZE IN REMOTE MEDIATOR")
        return InitializeAction.SKIP_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<String, Message>
    ): MediatorResult {
        Log.wtf("MARIOCH","Called Mediator")
        val data = when (loadType) {
            LoadType.REFRESH -> {
                Log.wtf("MARIOCH","Calling refresh (Remote Mediator)")
                null }
            LoadType.APPEND -> {
                Log.wtf("MARIOCH","Calling append (Remote Mediator)")
                try {
                fireStoreDb.getAppendMessages(dbWrapper.getRemoteKey().nextKey ?: "no-key")
                } catch (e: Throwable) {
                    Log.wtf("MARIOCH", "GET MESSAGES THREW AN EXCEPTION $e")
                    emptyList()
                }

            }
            LoadType.PREPEND -> {
                Log.wtf("MARIOCH","Calling prepend (Remote Mediator)")

                    try {
                        fireStoreDb.getPrependMessages(dbWrapper.getRemoteKey().prevKey ?: "no-key")
                    } catch (e: Throwable) {
                        Log.wtf("MARIOCH", "GET MESSAGES THREW AN EXCEPTION $e")
                        emptyList()
                    }

            }
        }
        dbWrapper.insertAllMessages(data)
        Log.wtf("MARIOCH","Mediator finished with enDofPagination: ${data?.isEmpty() ?: false}")
       return MediatorResult.Success(endOfPaginationReached = data?.isEmpty() ?: false)
    }
}