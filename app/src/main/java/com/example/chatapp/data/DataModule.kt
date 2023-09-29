package com.example.chatapp.data

import com.example.chatapp.domain.repository.MessageRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    fun provideMessageRepository(messageRepositoryImpl: MessageRepositoryImpl): MessageRepository
}