package com.ravnnerdery.data.network.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.ravnnerdery.data.network.models.MessageNetwork
import kotlinx.coroutines.flow.Flow


interface FirestoreDb {
    fun getMessageEvents(key: Pair<String,String?>?): Flow<List<DocumentSnapshot>>
    fun addMessage(message: String)
    suspend fun getMoreMessages(key: Pair<String, Long>?): List<DocumentSnapshot>
    suspend fun getFirstMessages(): List<DocumentSnapshot>
    suspend fun getAppendMessages(key: String): List<DocumentSnapshot>
    suspend fun getPrependMessages(key: String): List<DocumentSnapshot>
}