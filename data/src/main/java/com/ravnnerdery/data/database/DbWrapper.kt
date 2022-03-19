package com.ravnnerdery.data.database

import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.google.firebase.firestore.DocumentSnapshot
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.database.models.RemoteKeyEntity
import com.ravnnerdery.domain.other.Constants.MSG_REMOTE_KEY
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
    suspend fun getLastMessages(limit: Int): List<MessageEntity>
    // REMOTE KEYS
    suspend fun updNextRemoteKey(nextKey: String)
    suspend fun updPrevRemoteKey(prevKey: String)
    suspend fun getRemoteKey(): RemoteKeyEntity
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
            Log.wtf("MARIOCH","calledappenddbfunction with key: $key")
            databaseDao.getMessagesForAppend(key, limit)
        }
    }

    override suspend fun getMessagesForPrepend(key: String, limit: Int): List<MessageEntity> {
        return withContext(Dispatchers.IO) {
            Log.wtf("MARIOCH","calledprependdbfunction with key: $key")
                databaseDao.getMessagesForPrepend(key, limit)
        }
    }


    override suspend fun getLastMessages(limit: Int): List<MessageEntity> {
        return withContext(Dispatchers.IO) {
            Log.wtf("MARIOCH","calledrefreshdbfunction")
            databaseDao.getLastMessages(limit)
        }
    }

    /**
     * <<<<<<<<<< REMOTE KEYS >>>>>>>>>>>
     */

    override suspend fun updNextRemoteKey(nextKey: String) {
        withContext(Dispatchers.IO){
            databaseDao.updateNextKeysOnRemoteKey(MSG_REMOTE_KEY,nextKey)
        }
    }

    override suspend fun updPrevRemoteKey(prevKey: String) {
        withContext(Dispatchers.IO){
            databaseDao.updatePreviousKeysOnRemoteKey(MSG_REMOTE_KEY,prevKey)
        }
    }

    override suspend fun getRemoteKey(): RemoteKeyEntity {
        return withContext(Dispatchers.IO) {
            databaseDao.getRemoteKey(MSG_REMOTE_KEY)
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