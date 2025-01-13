package com.example.websocketchatapp.data.remote

import com.example.websocketchatapp.domain.model.Message
import com.example.websocketchatapp.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {
    suspend fun initSession(username:String):Resource<Unit>

    suspend fun sendMessage(msg:String)

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()

    companion object{
        const val BASE_URL = "ws://192.168.31.43:8082"
    }

    sealed class Endpoints(val url:String){
        object ChatSocketRoute : Endpoints("$BASE_URL/chat-socket")
    }
}