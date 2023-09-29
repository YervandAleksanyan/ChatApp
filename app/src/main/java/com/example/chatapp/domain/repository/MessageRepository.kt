package com.example.chatapp.domain.repository

import com.example.chatapp.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun getCommonMessages(): Flow<Message.CommonMessage>

    suspend fun getRareMessages(): Flow<Message.RareMessage>

    suspend fun getOwnMessages(): Flow<Message.MyMessage>

    suspend fun sendMessage(message: String)
}