package dev.cianjur.pagging3.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.cianjur.pagging3.data.PhotoDb

@Dao
interface PhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(photos: List<PhotoDb>)

    @Query(
        "SELECT * FROM photos WHERE searchQuery LIKE :queryString"
    )
    fun searchPhotos(queryString: String): PagingSource<Int, PhotoDb>

    @Query("DELETE FROM photos")
    suspend fun clearPhotos()

    @Query("DELETE FROM photos WHERE searchQuery LIKE :queryString")
    fun deleteByQuery(queryString: String)
}