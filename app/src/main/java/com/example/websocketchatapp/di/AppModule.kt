package com.example.websocketchatapp.di

import com.example.websocketchatapp.data.remote.ChatSocketService
import com.example.websocketchatapp.data.remote.ChatSocketServiceImpl
import com.example.websocketchatapp.data.remote.MessageService
import com.example.websocketchatapp.data.remote.MessageServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import javax.inject.Singleton
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient():HttpClient{
        return HttpClient(CIO){
            install(Logging)
            install(WebSockets)
            install(ContentNegotiation){

            }
        }
    }

    @Provides
    @Singleton
    fun provideMessageService(client:HttpClient):MessageService{
        return MessageServiceImpl(client)
    }

    @Provides
    @Singleton
    fun provideChatSocketService(client:HttpClient):ChatSocketService{
        return ChatSocketServiceImpl(client)
    }
}