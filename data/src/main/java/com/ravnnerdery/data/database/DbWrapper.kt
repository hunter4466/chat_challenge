package com.ravnnerdery.data.database

import android.util.Log
import androidx.paging.PagingSource
import com.google.firebase.firestore.DocumentSnapshot
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.network.firestore.FirestoreDb
import com.ravnnerdery.domain.other.Constants.FIREBASE_MESSAGE_ID
import kotlinx.coroutines.*
import javax.inject.Inject

interface DbWrapper {
    // MESSAGES
    suspend fun insertAllMessages(messages: List<DocumentSnapshot>?)
    suspend fun getLastMessages(limit: Int): List<MessageEntity>
    fun getPaginatedMessages(): PagingSource<Int, MessageEntity>
    suspend fun insertNewMessage(message: String, id: String)

    // REMOTE KEYS
    suspend fun getKeyForPrepend(): String
    suspend fun getKeyForAppend(): String

    // DB UPDATE
    suspend fun updateDatabaseUpdatedAt()
    suspend fun getUpdatedDatabaseTime(): Long
}

class DbWrapperImpl @Inject constructor(
    private val databaseDao: DatabaseDao,
    private val database: MessagesDatabase,
    private val fireStoreDb: FirestoreDb
) : DbWrapper {
    private val viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * <<<<<<<<<< OBSERVERS >>>>>>>>>>>
     */
    init {
        uiScope.launch(Dispatchers.IO) {
            fireStoreDb.getMessageEvents().collect {
                if (it.isNotEmpty()) {
                    val newMessage = it[0].get("message").toString()
                    insertNewMessage(newMessage, it[0].id)
                }
            }
        }
    }

    /**
     * <<<<<<<<<< MESSAGES >>>>>>>>>>>
     */


    override suspend fun insertAllMessages(messages: List<DocumentSnapshot>?) {
        withContext(Dispatchers.IO) {
            messages?.forEach { message ->
                databaseDao.insertMessage(
                    MessageEntity(
                        messageId = message.id,
                        message = message.get("message") as String,
                        time = message.get("time") as Long,
                        userId = message.get("userId") as String,
                        image = message.get("image") as String,
                        read = message.get("read") as Boolean
                    )
                )
                Log.v(
                    "MARIOCH",
                    "Added some data with this items: ${message.id}, ${message.get("message")}, ${
                        message.get("time")
                    }, ${message.get("userId")}, ${message.get("image")}, ${message.get("read")}"
                )
            }
        }
        updateDatabaseUpdatedAt()
    }


    override suspend fun getLastMessages(limit: Int): List<MessageEntity> {
        return withContext(Dispatchers.IO) {
            Log.wtf("MARIOCH", "calledrefreshdbfunction")
            databaseDao.getLastMessages(limit)
        }
    }

    override fun getPaginatedMessages(): PagingSource<Int, MessageEntity> {
        return databaseDao.getPaginatedMessages()
    }

    override suspend fun insertNewMessage(message: String, id: String) {
        withContext(Dispatchers.IO) {
            databaseDao.insertMessage(
                MessageEntity(
                    messageId = id,
                    message = message,
                    time = System.currentTimeMillis(),
                    userId = FIREBASE_MESSAGE_ID,
                    image = "https://i.picsum.photos/id/391/200/300.jpg?hmac=3xP-y2PRN2E0__aPOCp1sja7XrimKgLQAMiSaNd0Cko",
                    read = false,
                )
            )
        }
        updateDatabaseUpdatedAt()
    }

    /**
     * <<<<<<<<<< REMOTE KEYS >>>>>>>>>>>
     */

    override suspend fun getKeyForPrepend(): String {
        return withContext(Dispatchers.IO) {
            databaseDao.getKeyForPrepend()
        }
    }

    override suspend fun getKeyForAppend(): String {
        return withContext(Dispatchers.IO) {
            databaseDao.getKeyForAppend()
        }
    }

    /**
     * <<<<<<<<<< DB UPDATE >>>>>>>>>>>
     */

    override suspend fun updateDatabaseUpdatedAt() {
        database.updateDatabaseUpdatedAt()
    }

    override suspend fun getUpdatedDatabaseTime(): Long {
        return database.lastUpdated
    }
}