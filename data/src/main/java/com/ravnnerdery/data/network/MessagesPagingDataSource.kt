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
        when (params) {
            is LoadParams.Refresh -> {
                Log.wtf("MARIOCH","refresh (Paging Source)")
                type = "Refresh"
                nextKey = params.key
                previousKey = params.key
            }
            is LoadParams.Append -> {
                Log.wtf("MARIOCH","append (Paging Source)")
                type = "Append"
                nextKey = params.key
            }
            is LoadParams.Prepend -> {
                Log.wtf("MARIOCH","prepend (Paging Source)")
                type = "Prepend"
                previousKey = params.key
            }
        }
        val data = when {
            nextKey != null -> dbWrapper.getMessagesForAppend(nextKey, limit).map{it.mapToDomainModel()}
            previousKey != null -> dbWrapper.getMessagesForPrepend(previousKey, limit).map{it.mapToDomainModel()}
            else -> emptyList()
        }
        Log.wtf("MARIOCH","on load keys($type): N(${nextKey}) P(${previousKey})")
        return LoadResult.Page(data, nextKey, previousKey)
    }

}





















//class MessagesPagingDataSource @Inject constructor(
//    private val dbWrapper: DbWrapper
//) : PagingSource<String, Message>() {
//    private var jumpFactor: String = "10"
//    override fun getRefreshKey(state: PagingState<String, Message>): String {
//        Log.wtf("MARIOCH", "CALLED REFRESH KEY IN PAGING SOURCE")
//        return state.anchorPosition.toString()
//    }
//    init {
//        Log.wtf("MARIOCH","PAGING DATA SOURCE INITIALIZED")
//    }
//
//    override suspend fun load(params: LoadParams<String>): LoadResult<String, Message> {
//        Log.wtf("MARIOCH", "TRYING TO PULL DATA")
//        when(params){
//            is LoadParams.Refresh -> Log.wtf("MARIOCH", "LOAD IS IN REFRESH")
//            is LoadParams.Prepend -> Log.wtf("MARIOCH", "LOAD IS IN PREPEND")
//            is LoadParams.Append -> Log.wtf("MARIOCH", "LOAD IS IN APPEND")
//        }
//        val nextKey: String? = params.key
//        var previousPageIndex: String? = null
//        var nextPageIndex: String? = null
//        val response = try {
//            if (nextKey == null) {
//                Log.wtf("MARIOCH", "TRYED FIRST LOAD")
//                val res = dbWrapper.getLastReadMessages(jumpFactor.toInt())
//                if (res.isNotEmpty()) {
//                    dbWrapper.updateNextKeysOnRemoteKey(
//                        key = "messagesRemoteKey",
//                        nextKey = res.last().messageId,
//                        nextKeyTime = "${res.last().time},next"
//                    )
//                    dbWrapper.updatePreviousKeysOnRemoteKey(
//                        key = "messagesRemoteKey",
//                        prevKey = res.first().messageId,
//                        prevKeyTime = "${res.first().time},prev"
//                    )
//                    previousPageIndex = "${res.first().time},prev"
//                    nextPageIndex = "${res.last().time},next"
//                }
//                res
//            } else {
//                val splitKey = nextKey.split(",")
//                if (splitKey[1] == "next") {
//                    Log.wtf("MARIOCH", "TRYED SECOND LOAD with next")
//                    delay(1000)
//                    val res = dbWrapper.getPaginatedNextMessages(
//                        splitKey[0].toLong(),
//                        jumpFactor.toInt()
//                    )
//                    if (res.isNotEmpty()) {
//                        dbWrapper.updateNextKeysOnRemoteKey(
//                            key = "messagesRemoteKey",
//                            nextKey = res.last().messageId,
//                            nextKeyTime = "${res.last().time},next"
//                        )
//                        dbWrapper.updatePreviousKeysOnRemoteKey(
//                            key = "messagesRemoteKey",
//                            prevKey = res.first().messageId,
//                            prevKeyTime = "${res.first().time},prev"
//                        )
//                        nextPageIndex = "${res.last().time},next"
//                    }
//                    res
//                } else {
//                    Log.wtf("MARIOCH", "TRYED SECOND LOAD with prev")
//                    delay(1000)
//                    val res = dbWrapper.getPaginatedPrevMessages(
//                        splitKey[0].toLong(),
//                        jumpFactor.toInt()
//                    )
//                    if (res.isNotEmpty()) {
//                        dbWrapper.updateNextKeysOnRemoteKey(
//                            key = "messagesRemoteKey",
//                            nextKey = res.last().messageId,
//                            nextKeyTime = "${res.last().time},next"
//                        )
//                        dbWrapper.updatePreviousKeysOnRemoteKey(
//                            key = "messagesRemoteKey",
//                            prevKey = res.first().messageId,
//                            prevKeyTime = "${res.first().time},prev"
//                        )
//                        previousPageIndex = "${res.first().time},prev"
//                    }
//                    res
//                }
//            }
//        } catch (e: Exception) {
//            Log.v("MARIOCH", "EXC: $e")
//            emptyList()
//        }
//        val data = response.map { it.mapToDomainModel() }
//
//        Log.wtf("MARIOCH", "RESPONSE IS NOT EMPTY AND HAS prev ${previousPageIndex} next $nextPageIndex KEYS")
//
//        return LoadResult.Page(
//            data = data,
//            prevKey = previousPageIndex,
//            nextKey = nextPageIndex,
//        )
//    }
//}