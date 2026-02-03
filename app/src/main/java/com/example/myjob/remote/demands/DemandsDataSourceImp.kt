package com.example.myjob.remote.demands

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.demands.MarketDemandModel
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import javax.inject.Inject

class DemandsDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : DemandsDataSource {

    override suspend fun saveDemand(
        marketDemandModel: MarketDemandModel
    ): UserResponse  = apiService.saveDemand(marketDemandModel)

    override suspend fun countDownTrial(idDemand: Int): UserResponse = apiService.countDownTrial(idDemand)

    override suspend fun getAllDemands(
        pageNumber: Int
    ): GenericResponse<MarketDemandModel> = apiService.getAllDemands(pageNumber)


}