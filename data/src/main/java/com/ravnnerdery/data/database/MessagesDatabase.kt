package com.ravnnerdery.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ravnnerdery.data.database.models.MessageEntity

@Database(entities = [MessageEntity::class], version = 9, exportSchema = false)
abstract class MessagesDatabase : RoomDatabase() {
    var lastUpdated = System.currentTimeMillis()
    abstract fun databaseDao(): DatabaseDao
    fun updateDatabaseUpdatedAt() {
        lastUpdated = System.currentTimeMillis()
    }
}