package com.example.myjob.remote.source.avis

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.Rate
import com.example.myjob.domain.response.UserResponse
import com.example.myjob.remote.api.ApiService
import javax.inject.Inject

class RateDataSourceImp @Inject constructor(
    private val apiService: ApiService
) : RateDataSource {
    override suspend fun saveAvis(rate: Rate): UserResponse = apiService.saveAvis(rate)

    override suspend fun getCandidateAvis(
        idCandidate: Int,
        pageNumber: Int
    ): GenericResponse<Rate> = apiService.getCandidateAvis(idCandidate, pageNumber)
}