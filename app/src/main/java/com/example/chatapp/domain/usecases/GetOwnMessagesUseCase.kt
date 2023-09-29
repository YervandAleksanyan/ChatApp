package com.example.chatapp.domain.usecases

import com.example.chatapp.domain.model.Message
import com.example.chatapp.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetOwnMessagesUseCase @Inject constructor(
    private val messagesRepository: MessageRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(): Flow<Message.MyMessage> =
        withContext(coroutineDispatcher) {
            messagesRepository.getOwnMessages()
        }
}