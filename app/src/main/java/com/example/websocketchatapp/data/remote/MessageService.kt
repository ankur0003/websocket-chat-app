package com.example.websocketchatapp.data.remote

import com.example.websocketchatapp.domain.model.Message

interface MessageService {
    suspend fun getAllMessages():List<Message>
    companion object{
        const val BASE_URL = "http://192.168.31.43:8082"
    }

    sealed class Endpoints(val url:String){
        object GetAllMessages : Endpoints("$BASE_URL/messages")
    }
}