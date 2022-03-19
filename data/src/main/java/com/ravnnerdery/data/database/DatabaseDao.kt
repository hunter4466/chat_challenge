package com.ravnnerdery.data.database

import androidx.room.*
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.database.models.RemoteKeyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {
    /**
     * <<<<<< MESSAGES >>>>>>>
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(messageEntity: MessageEntity)

    @Query("SELECT * FROM messages_table WHERE TIME > (SELECT time FROM messages_table WHERE messageId = :key) ORDER BY time ASC LIMIT :limit")
    fun getMessagesForAppend(key: String, limit: Int): List<MessageEntity>

    @Query("SELECT * FROM (SELECT * FROM messages_table WHERE time < (SELECT TIME FROM messages_table WHERE messageId = :key ) ORDER BY TIME DESC LIMIT :limit) ORDER BY TIME ASC")
    fun getMessagesForPrepend(key: String, limit: Int): List<MessageEntity>

    @Query("SELECT * FROM(SELECT * FROM messages_table ORDER BY time DESC LIMIT 5) ORDER BY time ASC")
    fun getLastMessages(): List<MessageEntity>

    @Query("SELECT * FROM messages_table")
    fun observeTables(): Flow<List<MessageEntity>>


    /**
    * <<<<<< REMOTE KEYS >>>>>>>
    */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRemoteKey(remoteKey: RemoteKeyEntity)

    @Query("UPDATE remote_keys_table SET nextKey = :nextKey WHERE keyName = :key")
    fun updateNextKeysOnRemoteKey(key: String, nextKey: Long)

    @Query("UPDATE remote_keys_table SET prevKey = :prevKey WHERE keyName = :key")
    fun updatePreviousKeysOnRemoteKey(key: String, prevKey: Long)

    @Query("SELECT * FROM remote_keys_table WHERE keyName = :keyName LIMIT 1")
    fun getRemoteKey(keyName: String): RemoteKeyEntity
}