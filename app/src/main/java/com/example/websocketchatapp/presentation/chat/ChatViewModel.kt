package com.example.websocketchatapp.presentation.chat

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.websocketchatapp.data.remote.ChatSocketService
import com.example.websocketchatapp.data.remote.MessageService
import com.example.websocketchatapp.domain.model.Message
import com.example.websocketchatapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _msgText = mutableStateOf("")
    val msgText: State<String> = _msgText

    private val _msgTextState = mutableStateOf(ChatState())
    val msgTextState :State<ChatState> = _msgTextState

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()


    fun connectToChat() {
        getAllMessages()
        savedStateHandle.get<String>("username")?.let{username->
            viewModelScope.launch {
                val result  = chatSocketService.initSession(username)
                when(result){
                    is Resource.Success->{
                        chatSocketService.observeMessages().onEach { message->
                            val newList = msgTextState.value.messages.toMutableList().apply {
                                add(0,message)
                            }
                            _msgTextState.value = msgTextState.value.copy(messages = newList)
                        }.launchIn(viewModelScope)
                    }
                    is Resource.Error ->{
                        _toastEvent.emit(result.message?:"Unknown error")
                    }

                }
            }
        }
    }
    fun onMessageChanged(message: String){
        _msgText.value = message
    }

    fun disconnect(){
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }
    fun sendMessage(){
        viewModelScope.launch {
            if(msgText.value.isNotBlank()){
                chatSocketService.sendMessage(msgText.value)
            }
        }
    }
    fun getAllMessages(){
        viewModelScope.launch {

            _msgTextState.value = msgTextState.value.copy(isLoading = true)
            val result = messageService.getAllMessages()
            _msgTextState.value = msgTextState.value.copy(messages = result,isLoading = false)

        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}