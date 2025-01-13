package com.example.websocketchatapp.data.remote

import com.example.websocketchatapp.data.remote.dto.MessageDto
import com.example.websocketchatapp.domain.model.Message
import com.example.websocketchatapp.util.Resource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ChatSocketServiceImpl(private val client: HttpClient) :ChatSocketService {
    private var socket: WebSocketSession?=null
    override suspend fun initSession(username: String): Resource<Unit> {
        return try{
            socket = client.webSocketSession{
                url("${ChatSocketService.Endpoints.ChatSocketRoute.url}?username=$username")
            }
            if(socket?.isActive ==true){
                Resource.Success(Unit)
            }else{
                Resource.Error("could not establish connection")
            }
        }catch (e:Exception){
            e.printStackTrace()
            Resource.Error(e.localizedMessage?:"unknown error")
        }
    }

    override suspend fun sendMessage(msg: String) {
         try{
            socket?.send(Frame.Text(msg))
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun observeMessages(): Flow<Message> {
        return try{
            socket?.incoming?.receiveAsFlow()?.filter { it is Frame.Text }?.map {
                val json = (it as? Frame.Text)?.readText() ?: ""
                val msgDto = Json.decodeFromString<MessageDto>(json)
                msgDto.toMessage()
            }?: emptyFlow()
        }catch (e:Exception){
            e.printStackTrace()
            emptyFlow()
        }
    }

    override suspend fun closeSession() {
        socket?.close()
    }
}