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

class SaveMessageUseCase @Inject constructor(
    private val repository: ChatRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : FlowBaseUseCase<ChatMessage, ChatMessage>() {

    override suspend fun buildRequest(params: ChatMessage?): Flow<Resource<ChatMessage>> {
        return repository.saveMessage(params ?: ChatMessage()).flowOn(dispatcher)
    }
}