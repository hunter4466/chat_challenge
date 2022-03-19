package com.ravnnerdery.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ravnnerdery.data.database.models.MessageEntity
import com.ravnnerdery.data.database.models.RemoteKeyEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

@Database(entities = [MessageEntity::class, RemoteKeyEntity::class], version = 9, exportSchema = false)
abstract class MessagesDatabase : RoomDatabase() {
    var lastUpdated: Long = System.currentTimeMillis()
    abstract fun databaseDao() : DatabaseDao
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
}