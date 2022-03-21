package com.ravnnerdery.data.database

import androidx.paging.PagingSource
import androidx.room.*
import com.ravnnerdery.data.database.models.MessageEntity

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

    @Query("SELECT * FROM(SELECT * FROM messages_table WHERE read = 1 ORDER BY time DESC LIMIT :limit) ORDER BY time ASC")
    fun getLastMessages(limit: Int): List<MessageEntity>

    @Query("SELECT * FROM MESSAGES_TABLE ORDER BY time DESC")
    fun getPaginatedMessages(): PagingSource<Int, MessageEntity>

    /**
     * <<<<<< REMOTE KEYS >>>>>>>
     */

    @Query("SELECT messageId FROM messages_table ORDER BY time DESC LIMIT 1")
    fun getKeyForPrepend(): String

    @Query("SELECT messageId FROM messages_table ORDER BY time ASC LIMIT 1")
    fun getKeyForAppend(): String
}