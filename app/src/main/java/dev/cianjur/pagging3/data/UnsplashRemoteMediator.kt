package dev.cianjur.pagging3.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import dev.cianjur.pagging3.api.UnsplashApi
import dev.cianjur.pagging3.db.PhotoDatabase
import dev.cianjur.pagging3.db.RemoteKeys
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class UnsplashRemoteMediator(
    private val query: String,
    private val api: UnsplashApi,
    private val database: PhotoDatabase,
) : RemoteMediator<Int, PhotoDb>() {

    private val photosDao = database.photosDao()
    private val remoteKeyDao = database.remoteKeysDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, PhotoDb>): MediatorResult {
        return try {
            // The network load method takes an optional String
            // parameter. For every page after the first, pass the String
            // token returned from the previous page to let it continue
            // from where it left off. For REFRESH, pass null to load the
            // first page.
            val loadKey = when (loadType) {
                LoadType.REFRESH -> UNSPLASH_STARTING_PAGE_INDEX
                // In this example, you never need to prepend, since REFRESH
                // will always load the first page in the list. Immediately
                // return, reporting end of pagination.
                LoadType.PREPEND -> return MediatorResult.Success(
                    endOfPaginationReached = true
                )
                // Query remoteKeyDao for the next RemoteKey.
                LoadType.APPEND -> {
                    val remoteKey = database.withTransaction {
                        remoteKeyDao.remoteKeyByQuery(query)
                    }

                    // You must explicitly check if the page key is null when
                    // appending, since null is only valid for initial load.
                    // If you receive null for APPEND, that means you have
                    // reached the end of pagination and there are no more
                    // items to load.
                    if (remoteKey?.nextKey == null) {
                        return MediatorResult.Success(
                            endOfPaginationReached = true
                        )
                    }

                    remoteKey.nextKey
                }
            }

            // Suspending network load via Retrofit. This doesn't need to
            // be wrapped in a withContext(Dispatcher.IO) { ... } block
            // since Retrofit's Coroutine CallAdapter dispatches on a
            // worker thread.
            val response = api.searchPhotos(query, loadKey, state.config.pageSize)
            delay(3000)
            Log.e("REZAAA", "response ${response.results.size} $loadType == $loadKey")

            // Store loaded data, and next key in transaction, so that
            // they're always consistent.
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    remoteKeyDao.deleteByQuery(query)
                    photosDao.deleteByQuery(query)
                }

                // Update RemoteKey for this query.
                remoteKeyDao.insertOrReplace(
                    RemoteKeys(query, loadKey + 1)
                )

                // Insert new users into database, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                photosDao.insertAll(response.results.map {
                    PhotoDb(
                        it.id,
                        it.description,
                        it.urls.full,
                        it.urls.regular,
                        it.user.name,
                        it.user.username,
                        query
                    )
                })
            }

            MediatorResult.Success(
                endOfPaginationReached = response.total_pages == loadKey
            )
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}
