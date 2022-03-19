package com.ravnnerdery.data.network

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.ravnnerdery.data.database.DbWrapper
import com.ravnnerdery.data.database.DbWrapperImpl
import com.ravnnerdery.data.database.models.RemoteKeyEntity
import com.ravnnerdery.domain.models.Message
import com.ravnnerdery.domain.other.Constants.HOLDING_KEY
import com.ravnnerdery.domain.other.Constants.INITIAL_KEY
import com.ravnnerdery.domain.other.Constants.INITIAL_NEXT_KEY
import com.ravnnerdery.domain.other.Constants.INITIAL_PREV_KEY
import com.ravnnerdery.domain.other.Constants.MSG_REMOTE_KEY
import kotlinx.coroutines.*
import javax.inject.Inject

class MessagesPagingDataSource @Inject constructor(
    private val dbWrapper: DbWrapper,
) : PagingSource<String, Message>() {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        Log.wtf("MARIOCH", "CALLED INITIALIZE IN PAGING SOURCE")
        uiScope.launch {
            dbWrapper.insertRemoteKey(MSG_REMOTE_KEY, INITIAL_NEXT_KEY, INITIAL_PREV_KEY)
            val observer = dbWrapper.observeTablesProvider().collect{
                Log.wtf("MARIOCH","OBSERVER COLLECTED DATA -> ${it}")
                if(it){
                    invalidate()
                }
            }
        }
        registerInvalidatedCallback {
            dbWrapper.removeObservables()
        }
    }

    override fun getRefreshKey(state: PagingState<String, Message>): String? = null

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Message> {
        Log.wtf("MARIOCH","Calling Load (Paging Source)")
        var type = "Unknown"
        val limit = 10
        var nextKey: String? = null
        var previousKey: String? = null
        val data = when (params) {
            is LoadParams.Refresh -> {
                Log.wtf("MARIOCH","refresh (Paging Source)")
                type = "Refresh"
                val res = dbWrapper.getLastMessages(limit).map{
                    Log.wtf("MARIOCH","Loaded ${it.message} in refresh")
                    it.mapToDomainModel() }
                if (res.isNotEmpty()) {
                    nextKey = res.last().messageId
                    previousKey = res.first().messageId
                    dbWrapper.updNextRemoteKey(nextKey)
                    dbWrapper.updPrevRemoteKey(previousKey)
                }
                res
            }
            is LoadParams.Append -> {
                Log.wtf("MARIOCH","append (Paging Source)")
                type = "Append"
                val res = dbWrapper.getMessagesForAppend(params.key, limit).map {
                    Log.wtf("MARIOCH","Loaded ${it.message} in append")
                    it.mapToDomainModel() }
                if (res.isNotEmpty()) {
                    nextKey = res.last().messageId
                    dbWrapper.updNextRemoteKey(nextKey)
                }
                res
            }
            is LoadParams.Prepend -> {
                Log.wtf("MARIOCH","prepend (Paging Source)")
                type = "Prepend"
                val res = dbWrapper.getMessagesForPrepend(params.key, limit).map {
                    Log.wtf("MARIOCH","Loaded ${it.message} in prepend")
                    it.mapToDomainModel() }
                if (res.isNotEmpty()) {
                    previousKey = res.first().messageId
                    dbWrapper.updPrevRemoteKey(previousKey)
                }
                res
            }
        }
        Log.wtf("MARIOCH","on load keys($type): N(${nextKey}) P(${previousKey}), data: ${if(data.isEmpty()) "Is empty" else "have${data.size}items"}")
        return LoadResult.Page(data, previousKey, nextKey )
    }





//
//    override suspend fun load(params: LoadParams<String>): LoadResult<String, Message> {
//        Log.wtf("MARIOCH","Calling Load (Paging Source)")
//        var type = "Unknown"
//        val limit = 10
//        var nextKey: String? = null
//        var previousKey: String? = null
//        when (params) {
//            is LoadParams.Refresh -> {
//                Log.wtf("MARIOCH","refresh (Paging Source)")
//                type = "Refresh"
//            }
//            is LoadParams.Append -> {
//                Log.wtf("MARIOCH","append (Paging Source)")
//                type = "Append"
//                nextKey = params.key
//            }
//            is LoadParams.Prepend -> {
//                Log.wtf("MARIOCH","prepend (Paging Source)")
//                type = "Prepend"
//                previousKey = params.key
//            }
//        }
//        val data = when {
//            nextKey != null -> {
//                val res = dbWrapper.getMessagesForAppend(nextKey, limit).map {
//                    Log.wtf("MARIOCH","Loaded ${it.message} in append")
//                    it.mapToDomainModel() }
//                if (res.isNotEmpty()) {
//                    nextKey = res.last().messageId
//                }
//                res
//            }
//            previousKey != null -> {
//                val res = dbWrapper.getMessagesForPrepend(previousKey, limit).map {
//                    Log.wtf("MARIOCH","Loaded ${it.message} in prepend")
//                    it.mapToDomainModel() }
//                if (res.isNotEmpty()) { previousKey = res.first().messageId }
//                res
//            }
//            else -> {
//                val res = dbWrapper.getLastMessages(limit).map{
//                    Log.wtf("MARIOCH","Loaded ${it.message} in refresh")
//                    it.mapToDomainModel() }
//                if (res.isNotEmpty()) {
//                    nextKey = res.last().messageId
//                    previousKey = res.first().messageId
//                }
//                res
//            }
//        }
//        Log.wtf("MARIOCH","on load keys($type): N(${nextKey}) P(${previousKey})")
//        return LoadResult.Page(data, previousKey, nextKey )
//    }







}
