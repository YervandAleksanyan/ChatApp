package com.example.chatapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatapp.domain.model.Message
import com.example.chatapp.domain.usecases.GetCommonMessagesUseCase
import com.example.chatapp.domain.usecases.GetOwnMessagesUseCase
import com.example.chatapp.domain.usecases.GetRareMessagesUseCase
import com.example.chatapp.domain.usecases.SendMessageUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


class MessagesViewModel @Inject constructor(
    private val getCommonMessagesUseCase: GetCommonMessagesUseCase,
    private val getRareMessagesUseCase: GetRareMessagesUseCase,
    private val getOwnMessagesUseCase: GetOwnMessagesUseCase,
    private val sendMessageUseCase: SendMessageUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    init {
        viewModelScope.launch {
            getCommonMessagesUseCase().collect {
                _messages.emit(ArrayList(_messages.value+it))
            }
        }
        viewModelScope.launch {
            getRareMessagesUseCase().collect {
                _messages.emit(ArrayList(_messages.value+it))
            }
        }
        viewModelScope.launch {
            getOwnMessagesUseCase().collect {
                _messages.emit(ArrayList(_messages.value+it))
                Log.d("myLogs", "viewModel ${it.content}")
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            sendMessageUseCase(text)
        }
    }
}