package com.example.websocketchatapp.presentation.chat

import com.example.websocketchatapp.domain.model.Message

data class ChatState(val messages:List<Message> = emptyList(),val isLoading:Boolean=false)
