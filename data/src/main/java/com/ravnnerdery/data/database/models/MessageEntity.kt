package com.ravnnerdery.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.ravnnerdery.domain.models.Message
import com.ravnnerdery.data.mappers.DomainMapper

@Entity(tableName = "messages_table", indices = [Index(value = ["messageId"], unique = true)])
class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "messageId")
    val messageId: String,
    @ColumnInfo
    val message: String,
    @ColumnInfo
    val time: Long,
    @ColumnInfo
    val read: Boolean,
    @ColumnInfo
    val userId: String,
    @ColumnInfo
    val image: String?,
) : DomainMapper<Message> {
    override fun mapToDomainModel(): Message {
        return Message(message, time, read, userId, messageId, image)
    }
}