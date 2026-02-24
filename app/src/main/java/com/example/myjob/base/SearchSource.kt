package com.example.myjob.base

import android.net.http.HttpException
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myjob.domain.entities.CategoryModel
import com.example.myjob.domain.entities.User
import com.example.myjob.remote.source.search.SearchDataSource

class SearchSource(
    private val remoteDataSource: SearchDataSource,
    val categoryModel: CategoryModel,
    val onCountListener: (count: Int) -> Unit
) : PagingSource<Int, User>() {

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val currentPage = params.key ?: 1
            Log.i("fkdzlgjzjhgrzlg", "load: $categoryModel")

            val response = remoteDataSource.getUserServiceFilteredList(
                categoryModel = categoryModel,
                pageNumber = currentPage
            )
            val count = response.numberOfElements
            val data = response.content
            val responseData = mutableListOf<User>()
            responseData.addAll(data)

            onCountListener(count)
            Log.i("zrfrzhgrje", "uuid: $responseData")

            LoadResult.Page(
                data = responseData,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (data.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            Log.i("zrfrzhgrje", "ex: ${e.message}")

            LoadResult.Error(e)
        } catch (exception: HttpException) {
            Log.i("zrfrzhgrje", "ex1: ${exception.message}")

            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}