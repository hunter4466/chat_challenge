package com.ravnnerdery.data.database

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.database.models.RemoteKeyEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface DbWrapper {
    // OBSERVERS
    fun observeTablesProvider(): Flow<Boolean>
    fun removeObservables()
    // MESSAGES
    suspend fun insertAllMessages(messages: List<DocumentSnapshot>?)
    suspend fun getMessagesForAppend(key: String, limit: Int): List<MessageEntity>
    suspend fun getMessagesForPrepend(key: String, limit: Int): List<MessageEntity>
    suspend fun getLastMessages(): List<MessageEntity>
    // REMOTE KEYS
    suspend fun updateNextKeysOnRemoteKey(key: String, nextKey: Long)
    suspend fun updatePreviousKeysOnRemoteKey(key: String, prevKey: Long)
    suspend fun getRemoteKey(keyName: String): RemoteKeyEntity
    suspend fun insertRemoteKey(keyName: String, nextKey: String, prevKey: String)

}

class DbWrapperImpl @Inject constructor(
    private val databaseDao: DatabaseDao
) : DbWrapper {
    /**
     * <<<<<<<<<< OBSERVERS >>>>>>>>>>>
     */

    private val observeTables = MutableStateFlow(false)
    override fun observeTablesProvider(): Flow<Boolean> {
        return observeTables
    }

    override fun removeObservables() {
        Log.wtf("MARIOCH","<<<<<<<<<<<<TRYING TO REMOVE OBSERVABLE>>>>>>>>>>>>>>")
        observeTables.value = false
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
                Log.v("MARIOCH","Added some data with this items: ${message.id}, ${message.get("message")}, ${message.get("time")}, ${message.get("userId")}, ${message.get("image")}, ${message.get("read")}")
            }
            observeTables.value = true
        }
    }

    override suspend fun getMessagesForAppend(key: String, limit: Int): List<MessageEntity> {
        return withContext(Dispatchers.IO) {
            databaseDao.getMessagesForAppend(key, limit)
        }
    }

    override suspend fun getMessagesForPrepend(key: String, limit: Int): List<MessageEntity> {
        return withContext(Dispatchers.IO) {
            if (key == "0") {
                getLastMessages()
            } else {
                databaseDao.getMessagesForPrepend(key, limit)
            }
        }
    }


    override suspend fun getLastMessages(): List<MessageEntity> {
        return withContext(Dispatchers.IO) {
            databaseDao.getLastMessages()
        }
    }

    /**
     * <<<<<<<<<< REMOTE KEYS >>>>>>>>>>>
     */

    override suspend fun updateNextKeysOnRemoteKey(key: String, nextKey: Long) {
        withContext(Dispatchers.IO){
            databaseDao.updatePreviousKeysOnRemoteKey(key,nextKey)
        }
    }

    override suspend fun updatePreviousKeysOnRemoteKey(key: String, prevKey: Long) {
        withContext(Dispatchers.IO){
            databaseDao.updatePreviousKeysOnRemoteKey(key,prevKey)
        }
    }

    override suspend fun getRemoteKey(keyName: String): RemoteKeyEntity {
        return withContext(Dispatchers.IO) {
            databaseDao.getRemoteKey(keyName)
        }
    }

    override suspend fun insertRemoteKey(keyName: String, nextKey: String, prevKey: String) {
        withContext(Dispatchers.IO) {
            databaseDao.insertRemoteKey(
                RemoteKeyEntity(id = 0, keyName = keyName, nextKey = nextKey, prevKey = prevKey)
            )
        }
    }
}