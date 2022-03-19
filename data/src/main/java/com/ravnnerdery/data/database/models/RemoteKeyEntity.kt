package com.ravnnerdery.data.database.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys_table", indices = [Index(value = ["keyName"], unique = true)])
class RemoteKeyEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "keyName")
    val keyName: String,
    @ColumnInfo
    val nextKey: String?,
    @ColumnInfo
    val prevKey: String?,
)