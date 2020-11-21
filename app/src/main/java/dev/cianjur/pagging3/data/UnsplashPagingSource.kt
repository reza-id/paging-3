package dev.cianjur.pagging3.data

import android.util.Log
import androidx.paging.PagingSource
import dev.cianjur.pagging3.api.UnsplashApi
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

private const val UNSPLASH_STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val unsplashApi: UnsplashApi,
    private val query: String,
) : PagingSource<Int, PhotoDb>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoDb> {
        val position = params.key ?: UNSPLASH_STARTING_PAGE_INDEX

        return try {
            val response = unsplashApi.searchPhotos(query, position, params.loadSize)
            delay(2000)
            val photos = response.results
                .map { PhotoDb(it.id, it.description, it.urls.full, it.urls.regular, it.user.name, it.user.username, query) }
            Log.e("REZAAA", "${photos[0].id} == $position")

            LoadResult.Page(
                data = photos,
                prevKey = if (position == UNSPLASH_STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}