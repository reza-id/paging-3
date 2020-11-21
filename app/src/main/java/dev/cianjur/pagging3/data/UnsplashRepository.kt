package dev.cianjur.pagging3.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import dev.cianjur.pagging3.api.UnsplashApi
import dev.cianjur.pagging3.db.PhotoDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(private val unsplashApi: UnsplashApi, private val database: PhotoDatabase) {

//    fun getSearchResults(query: String) =
//        Pager(
//            config = PagingConfig(pageSize = 20, maxSize = 100, enablePlaceholders = false),
//            pagingSourceFactory = { UnsplashPagingSource(unsplashApi, query) }
//        ).liveData

    fun getSearchResults(query: String): LiveData<PagingData<PhotoDb>> {
        // appending '%' so we can allow other characters to be before and after the query string
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { database.photosDao().searchPhotos(dbQuery) }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                prefetchDistance = 5,
                initialLoadSize = 20
            ),
            remoteMediator = UnsplashRemoteMediator(
                query,
                unsplashApi,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }
}
