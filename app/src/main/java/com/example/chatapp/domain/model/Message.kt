package com.example.chatapp.domain.model

sealed class Message(val content: String, val id: Int) {

    class CommonMessage(content: String, id: Int) : Message(content, id)
    class RareMessage(content: String, id: Int) : Message(content, id)
    class MyMessage(content: String, id: Int) : Message(content, id)
}