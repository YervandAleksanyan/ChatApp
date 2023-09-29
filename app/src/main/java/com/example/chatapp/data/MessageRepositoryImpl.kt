package com.example.chatapp.data

import com.example.chatapp.domain.model.Message
import com.example.chatapp.domain.repository.MessageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor() : MessageRepository {

    companion object {
        val messages = listOf("Привет", "Как дела?", "Что делаешь?", "Напишу позже", "Позвони мне", "Ты где?", "Когда вернешсья?")
    }

    @Volatile
    private var count = 0

    private val ownMessages = MutableSharedFlow<Message.MyMessage>()
//    private val coldOwnMessages = ownMessages.asFlow

    override suspend fun getCommonMessages(): Flow<Message.CommonMessage> = flow {
        while(true) {
            delay(1000)
            emit(Message.CommonMessage(messages.random(), ++count))
        }
    }

    override suspend fun getRareMessages(): Flow<Message.RareMessage> = flow {
        while(true) {
            delay(2000)
            emit(Message.RareMessage(messages.random(), ++count))
        }
    }

    override suspend fun getOwnMessages(): Flow<Message.MyMessage> =
        ownMessages.asSharedFlow()

    override suspend fun sendMessage(message: String) {
        ownMessages.emit(Message.MyMessage(message, ++count))
    }
}