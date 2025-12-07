package com.example.myjob.domain.usecase.chat

import com.example.myjob.base.reources.Resource
import com.example.myjob.base.usecase.FlowBaseUseCase
import com.example.myjob.data.chat.ChatRepository
import com.example.myjob.domain.qualifiers.IoDispatcher
import com.example.myjob.feature.messagerie.ChatMessage
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FindConversationUseCase @Inject constructor(
    private val repository: ChatRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<List<ChatMessage>, Pair<Int, Int>>() {

    override suspend fun buildRequest(params: Pair<Int, Int>?): Flow<Resource<List<ChatMessage>>> {
        return repository.getConversation(
            params?.first ?: -1,
            params?.second ?: -1,
        ).flowOn(dispatcher)
    }
}