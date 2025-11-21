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

class FindConversationUseCase @Inject constructor(
    private val repository: ChatRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<Conversation, List<String>>() {

    override suspend fun buildRequest(params: List<String>?): Flow<Resource<Conversation>> {
        return repository.findOrCreateConversation(
            params?.get(0) ?: "",
            params?.get(1) ?: "",
            params?.get(2) ?: "",
            params?.get(3) ?: ""
        ).flowOn(dispatcher)
    }
}