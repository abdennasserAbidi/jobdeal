package com.example.myjob.domain.usecase.announcement

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.announcement.AnnouncementRepository
import com.example.myjob.domain.entities.announcement.CommentsPost
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.domain.response.UserResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AddCommentUseCase @Inject constructor(
    private val repository: AnnouncementRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<UserResponse, Pair<Int, CommentsPost>>() {

    override suspend fun buildRequest(params: Pair<Int, CommentsPost>?): Flow<Resource<UserResponse>> {
        return repository.addComment(
            params?.first ?: -1,
            params?.second ?: CommentsPost()
        ).flowOn(dispatcher)
    }
}