package com.io.telegram

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable

class TelegramHttpClient(
    botToken: String
) {
    private val basePath = "https://api.telegram.org/bot${botToken}"
    private val client = HttpClient(CIO){
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.BODY
        }
        install(JsonFeature){
            serializer = KotlinxSerializer(
                kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
            )
        }
    }

    suspend fun sendMessageFromBehavior(
        telegramBehaviour: TelegramBehaviour
    ): Deferred<TelegramResponse<MessageIdResponse>> = withContext(Dispatchers.IO) {
        return@withContext when (telegramBehaviour){
            is TelegramBehaviour.Send -> requestAsDeferred(
                this,
                telegramBehaviour.request,
                telegramBehaviour.delay
            )
            is TelegramBehaviour.UpdateBehaviour -> requestAsDeferred(
                this, 
                telegramBehaviour.body,
                telegramBehaviour.editMessageTextRequest, 
                telegramBehaviour.delay,
                telegramBehaviour.nextDelay
            )
        }
    }

    private suspend inline fun <reified T: Any> requestAsDeferred(
        coroutineScope: CoroutineScope, 
        body: TelegramRequest,
        time: Long = 0
    ): Deferred<TelegramResponse<T>>  {
        delay(time)
        return coroutineScope.async {
            client.post("$basePath/${body.path}") {
                this.body = body
                contentType(ContentType.Application.Json)
            }
        }
    }


    private suspend inline fun requestAsDeferred(
        coroutineScope: CoroutineScope,
        bodyBefore: TelegramRequest,
        bodyAfter: TelegramRequest.EditMessageTextRequest,
        startTime: Long,
        nextTime: Long
    ): Deferred<TelegramResponse<MessageIdResponse>> {
        val res = requestAsDeferred<MessageIdResponse>(coroutineScope, bodyBefore, startTime).await()
        delay(startTime + nextTime)

        requestAsDeferred<HttpResponse>(coroutineScope, bodyAfter.copy(message_id = res.result.message_id)).await()
        
        return coroutineScope.async { res }
    }
       
    @Serializable
    data class TelegramResponse<T>(
        val ok: Boolean,
        val result: T
    )
    
    @Serializable
    data class MessageIdResponse(
        val message_id: Int
    )
    
}