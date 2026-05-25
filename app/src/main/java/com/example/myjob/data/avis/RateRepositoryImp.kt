package com.example.myjob.data.avis

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.myjob.base.GenericSource
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.reources.ResourceState
import com.example.myjob.domain.entities.Rate
import com.example.myjob.domain.response.RatePercentageResponse
import com.example.myjob.local.database.SharedPreference
import com.example.myjob.remote.source.avis.RateDataSource
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RateRepositoryImp @Inject constructor(
    private val remoteDataSource: RateDataSource,
    private val sharedPreference: SharedPreference
) : RateRepository {
    override suspend fun saveAvis(rate: Rate): Flow<Resource<RatePercentageResponse>> = flow {
        try {
            val data = remoteDataSource.saveAvis(rate)
            emit(Resource(ResourceState.SUCCESS, data, null))
        } catch (ex: Exception) {
            emit(Resource(ResourceState.SUCCESS, null, ex.message))
        }
    }

    override suspend fun getCandidateAvis(
        idCandidate: Int,
    ): Flow<Resource<PagingData<Rate>>> = flow {
        val pager = Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 2),
            pagingSourceFactory = {
                GenericSource { currentPage ->
                    val avis = remoteDataSource.getCandidateAvis(
                        idCandidate = idCandidate,
                        pageNumber = currentPage
                    )
                    val json = Gson().toJson(avis.content)
                    sharedPreference.putString("jsonAvis", json)

                    avis
                }
            }
        ).flow.cachedIn(CoroutineScope(Dispatchers.IO))

        emitAll(
            pager.map { pagingData ->
                Resource(ResourceState.SUCCESS, pagingData, null)
            }
        )
    }.catch { ex ->
        emit(Resource(ResourceState.ERROR, null, ex.message))
    }
}