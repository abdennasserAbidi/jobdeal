package com.example.myjob.remote.source.avis

import com.example.myjob.base.GenericResponse
import com.example.myjob.domain.entities.Rate
import com.example.myjob.domain.response.UserResponse

interface RateDataSource {
    suspend fun saveAvis(rate: Rate): UserResponse
    suspend fun getCandidateAvis(idCandidate: Int, pageNumber: Int): GenericResponse<Rate>
}