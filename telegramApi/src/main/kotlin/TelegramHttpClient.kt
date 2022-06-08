package com.io.telegram

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
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
        install(ContentNegotiation) {
            json(json = jsonSetting)
        }
    }

    suspend fun getResponseWithoutResult(
        request: TelegramRequest,
        params: Map<String, Any>
    ): EmptyResponse = withContext(Dispatchers.IO){
        client.get("$basePath/${request.path}") {
            url {
                params.forEach { (key, value) ->
                    parameters.append(key, value.toString())
                }
            }
        }
        return@withContext EmptyResponse
    }

    suspend fun getResponse(
        request: TelegramRequest,
        params: Map<String, Any>
    ): Deferred<String> = withContext(Dispatchers.IO){
        return@withContext async {
            client.get("$basePath/${request.path}") {
                url {
                    params.forEach { (key, value) ->
                        parameters.append(key, value.toString())
                    }
                }
            }.body()
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
            is TelegramBehaviour.OrderSend -> TODO()
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
            client.post("$basePath/${body.path}") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        }
        return coroutineScope.async { TelegramResponse(ok = true, result = MessageIdResponse(message_id = deleteMessageid)) }
    }

    private suspend inline fun requestAsDeferred(
        coroutineScope: CoroutineScope,
        body: TelegramRequest,
        time: Long = 0
    ): Deferred<TelegramResponse<MessageIdResponse>>  {
        delay(time)
        return coroutineScope.async {
            client.post("$basePath/${body.path}") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
        }
    }

    private suspend inline fun orderRequestAsDeferred(
        coroutineScope: CoroutineScope,
        bodies: List<TelegramRequest>,
        time: Long = 0
    ): Deferred<TelegramResponse<MessageIdResponse>>  {
        delay(time)
        return coroutineScope.async {
            client.post("$basePath/${body.path}") {
                contentType(ContentType.Application.Json)
                setBody(body)
            }.body()
        }
    }
    
}