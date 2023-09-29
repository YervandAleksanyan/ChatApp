package com.example.chatapp.domain.usecases

import com.example.chatapp.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messagesRepository: MessageRepository,
    private val coroutineDispatcher: CoroutineDispatcher
) {

    suspend operator fun invoke(message: String) =
        withContext(coroutineDispatcher) {
            messagesRepository.sendMessage(message)
        }
}