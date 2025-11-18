package com.example.myjob.domain.usecase.chat

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.chat.ChatRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.feature.messagerie.Conversation
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserConversationsUseCase @Inject constructor(
    private val repository: ChatRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<List<Conversation>, String>() {

    override suspend fun buildRequest(params: String?): Flow<Resource<List<Conversation>>> {
        return repository.getUserConversations(params ?: "").flowOn(dispatcher)
    }
}