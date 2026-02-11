package com.example.myjob.data.demand

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.flow.Flow

interface DemandRepository {
    suspend fun saveDemand(marketDemandModel: MarketDemandModel): Flow<Resource<UserResponse>>
    suspend fun deleteDemand(idDemand: Int): Flow<Resource<UserResponse>>
    suspend fun countDownTrial(idDemand: Int): Flow<Resource<UserResponse>>
    suspend fun getAllDemands(): Flow<Resource<PagingData<MarketDemandModel>>>
    suspend fun getDemandFiltered(word: String): Flow<Resource<PagingData<MarketDemandModel>>>
    suspend fun getDemand(idDemand: Int): Flow<Resource<MarketDemandModel>>
}