package dev.cianjur.pagging3.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey
    val searchQuery: String,
    val nextKey: Int?
)
