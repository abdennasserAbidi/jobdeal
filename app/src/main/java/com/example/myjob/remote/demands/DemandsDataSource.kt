package com.example.myjob.remote.demands

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.response.UserResponse

interface DemandsDataSource {
    suspend fun saveDemand(marketDemandModel: MarketDemandModel): UserResponse
    suspend fun getAllDemands(pageNumber: Int): GenericResponse<MarketDemandModel>
}