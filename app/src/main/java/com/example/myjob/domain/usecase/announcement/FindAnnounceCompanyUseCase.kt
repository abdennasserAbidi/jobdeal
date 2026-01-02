package com.example.myjob.domain.usecase.announcement

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.announcement.AnnouncementRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.AnnounceResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FindAnnounceCompanyUseCase @Inject constructor(
    private val repository: AnnouncementRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<AnnounceResponse, Pair<String, Int>>() {

    override suspend fun buildRequest(params: Pair<String, Int>?): Flow<Resource<AnnounceResponse>> {
        return repository.findAnnounceCompany(
            params?.first ?: "",
            params?.second ?: -1
        ).flowOn(dispatcher)
    }
}