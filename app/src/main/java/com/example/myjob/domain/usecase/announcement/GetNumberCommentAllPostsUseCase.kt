package com.example.myjob.domain.usecase.announcement

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.announcement.AnnouncementRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetNumberCommentAllPostsUseCase @Inject constructor(
    private val repository: AnnouncementRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<List<Int>, Int>() {

    override suspend fun buildRequest(params: Int?): Flow<Resource<List<Int>>> {
        return repository.getNumberCommentAllPosts(
            params ?: -1
        ).flowOn(dispatcher)
    }
}