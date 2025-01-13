package com.example.websocketchatapp.data.remote

import android.util.Log
import com.example.websocketchatapp.data.remote.dto.MessageDto
import com.example.websocketchatapp.domain.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MessageServiceImpl(private val client: HttpClient) : MessageService {

    override suspend fun getAllMessages(): List<Message> {
        return try {
            val url = MessageService.Endpoints.GetAllMessages.url
            val response:String = client.get(url).body()

            val result = Json.decodeFromString<List<MessageDto>>(response)

            result.map { it.toMessage() }

//            client.get<List<MessageDto>>(MessageService.Endpoints.GetAllMessages.url).map {
//                it.toMessage()
//            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}