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
import kotlinx.serialization.json.Json

class TelegramHttpClient(
    botToken: String,
    isDebug: Boolean,
    jsonSetting: Json
) {
    private val basePath = "https://api.telegram.org/bot${botToken}"
    private val client = HttpClient(CIO){
        if (isDebug){
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.BODY
            }
        }
        install(JsonFeature){
            serializer = KotlinxSerializer(jsonSetting)
        }
    }

    suspend fun getResponseWithoutResult(
        request: TelegramRequest,
        params: Map<String, Any>
    ): EmptyResponse = withContext(Dispatchers.IO){
        client.get<HttpResponse>("$basePath/${request.path}") {
            params.forEach {
                parameter(it.key, it.value)
            }
        }
        return@withContext EmptyResponse
    }

    suspend fun getResponse(
        request: TelegramRequest,
        params: Map<String, Any>
    ): Deferred<String> = withContext(Dispatchers.IO){
        return@withContext async {
            client.get<String>("$basePath/${request.path}") {
                params.forEach {
                    parameter(it.key, it.value)
                }
            }
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
            is TelegramBehaviour.Delete -> deteleAsDeferred(
                this,
                telegramBehaviour.request,
                telegramBehaviour.deleteMessageId,
                telegramBehaviour.delay
            )
        }
    }

    private suspend inline fun deteleAsDeferred(
        coroutineScope: CoroutineScope,
        body: TelegramRequest,
        deleteMessageid: Int,
        time: Long = 0
    ): Deferred<TelegramResponse<MessageIdResponse>>  {
        delay(time)
        coroutineScope.launch {
            client.post<HttpResponse>("$basePath/${body.path}") {
                this.body = body
                contentType(ContentType.Application.Json)
            }
        }
        return coroutineScope.async { TelegramResponse(ok = true, result = MessageIdResponse(message_id = deleteMessageid)) }
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
    
}