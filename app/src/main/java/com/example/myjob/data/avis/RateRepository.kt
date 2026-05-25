package com.example.myjob.data.avis

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.Rate
import com.example.myjob.domain.response.RatePercentageResponse
import kotlinx.coroutines.flow.Flow

interface RateRepository {
    suspend fun saveAvis(rate: Rate): Flow<Resource<RatePercentageResponse>>
    suspend fun getCandidateAvis(idCandidate: Int): Flow<Resource<PagingData<Rate>>>
}