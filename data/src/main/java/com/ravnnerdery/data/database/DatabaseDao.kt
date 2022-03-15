package com.ravnnerdery.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ravnnerdery.data.database.models.MessageEntity

@Dao
interface DatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMessage(message: MessageEntity)

    @Query("SELECT * FROM messages_table ORDER BY time ASC LIMIT :limit OFFSET :offSet ")
    fun getPaginatedMessages(limit: Int, offSet: Int): List<MessageEntity>

}