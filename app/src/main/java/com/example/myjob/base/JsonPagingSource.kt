package com.example.myjob.base

import android.content.Context
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myjob.domain.entities.NewCountry
import com.google.gson.Gson

class JsonPagingSource(
    private val context: Context,
    private val jsonFileName: String
) : PagingSource<Int, NewCountry>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewCountry> {
        try {
            // Read JSON from the assets folder
            val json = context.assets.open(jsonFileName).bufferedReader().use { it.readText() }
            val items: List<NewCountry> = Gson().fromJson(json, Array<NewCountry>::class.java).toList()

            // Determine page parameters
            val currentPage = params.key ?: 0
            val pageSize = params.loadSize
            val start = currentPage * pageSize
            val end = (start + pageSize).coerceAtMost(items.size)

            val pageData = if (start < items.size) items.subList(start, end) else emptyList()

            return LoadResult.Page(
                data = pageData,
                prevKey = if (currentPage == 0) null else currentPage - 1,
                nextKey = if (end >= items.size) null else currentPage + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewCountry>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }
}