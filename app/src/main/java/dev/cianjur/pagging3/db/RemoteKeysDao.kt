package dev.cianjur.pagging3.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(remoteKey: RemoteKeys)

    @Query("SELECT * FROM remote_keys WHERE searchQuery = :searchQuery")
    suspend fun remoteKeyByQuery(searchQuery: String): RemoteKeys?

    @Query("DELETE FROM remote_keys WHERE searchQuery = :searchQuery")
    suspend fun deleteByQuery(searchQuery: String)
}
