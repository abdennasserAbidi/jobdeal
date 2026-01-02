package com.example.myjob.domain.usecase.announcement

import androidx.paging.PagingData
import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.announcement.AnnouncementRepository
import com.example.myjob.domain.entities.announcement.AnnouncementModel
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FindAnnounceCandidateUseCase @Inject constructor(
    private val repository: AnnouncementRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<PagingData<AnnouncementModel>, String>() {

    override suspend fun buildRequest(params: String?): Flow<Resource<PagingData<AnnouncementModel>>> {
        return repository.findAnnounceCandidate(params?: "").flowOn(dispatcher)
    }
}