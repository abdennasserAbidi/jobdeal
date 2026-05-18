package com.example.myjob.data.avis

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.domain.entities.Rate
import kotlinx.coroutines.flow.Flow

interface RateRepository {
    suspend fun saveAvis(rate: Rate): Flow<Resource<String>>
    suspend fun getCandidateAvis(idCandidate: Int): Flow<Resource<PagingData<Rate>>>
}