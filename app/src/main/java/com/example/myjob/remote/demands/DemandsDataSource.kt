package com.example.myjob.remote.demands

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.response.UserResponse

interface DemandsDataSource {
    suspend fun saveDemand(marketDemandModel: MarketDemandModel): UserResponse
    suspend fun countDownTrial(idDemand: Int): UserResponse
    suspend fun getAllDemands(pageNumber: Int): GenericResponse<MarketDemandModel>
    suspend fun getDemandFiltered(word: String, pageNumber: Int): GenericResponse<MarketDemandModel>
    suspend fun getDemand(idDemand: Int): MarketDemandModel
}