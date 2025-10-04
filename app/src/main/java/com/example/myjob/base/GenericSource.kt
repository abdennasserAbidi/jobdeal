package com.example.myjob.base

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myjob.common.GlobalEntries
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import retrofit2.HttpException
import java.io.IOException

open class GenericSource<T: Any> constructor(
    val paramsWS: Int = -1,
    val getData: suspend (currentPage: Int) -> GenericResponse<T>
): PagingSource<Int, T>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val currentPage = params.key ?: 1
        return try {

            val data = getData(currentPage)
            println("paramsWS: $paramsWS")

            /*if (paramsWS != -1) {
                val request = mapOf("id" to paramsWS, "page" to currentPage, "size" to 10)
                val jsonWS = Gson().toJson(request)

                GlobalEntries.socket?.send(jsonWS)?.collectLatest { message ->
                    println("Received message: $message")
                }
            }*/

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