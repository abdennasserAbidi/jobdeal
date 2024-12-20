package com.example.myjob.base

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import retrofit2.HttpException
import java.io.IOException

open class GenericSource<T: Any> constructor(
    val getData: suspend (currentPage: Int) -> GenericResponse<T>
): PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val currentPage = params.key ?: 1
        return try {

            val data = getData(currentPage)

            LoadResult.Page(
                 data = data.content,
                 prevKey = if (currentPage == 1) null else currentPage - 1,
                 nextKey = if (data.content.isEmpty()) null else currentPage + 1
             )
        } catch (exception: IOException) {
            Log.i("exception", "IOException: ${exception.message}")
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            Log.i("exception", "HttpException: ${exception.message}")
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        // Define a key to refresh the data
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}