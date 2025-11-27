package com.example.myjob.domain.usecase.announcement

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.announcement.AnnouncementRepository
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetCommentPostCompanyUseCase @Inject constructor(
    private val repository: AnnouncementRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<List<CommentsPost>, Pair<Int, Int>>() {

    override suspend fun buildRequest(params: Pair<Int, Int>?): Flow<Resource<List<CommentsPost>>> {
        return repository.getCommentAllPostsCompany(
            params?.first ?: -1,
            params?.second ?: -1
        ).flowOn(dispatcher)
    }
}