package com.ravnnerdery.data.network.firestore

import com.google.firebase.firestore.DocumentSnapshot
import com.ravnnerdery.data.network.models.MessageNetwork
import kotlinx.coroutines.flow.Flow


interface FirestoreDb {
    fun getMessageEvents(): Flow<List<DocumentSnapshot>>
    fun addMessage(message: String)
    suspend fun getFirstMessages(): List<DocumentSnapshot>
    suspend fun getAppendMessages(key: String): List<DocumentSnapshot>
    fun updateMessageToRead(id: String)
}