package com.io.telegram

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.json.Json

@Serializable
sealed class TelegramRequest(val path: String){

    fun asSendBehaviour(
        name: String,
        delay: Long = 0
    ): TelegramBehaviour.Send =
        TelegramBehaviour.Send(name,this, delay)

    //Setting

    object GetWebhookRequest: TelegramRequest("getWebhookInfo")
    object SetWebhookRequest: TelegramRequest("setWebhook")

    //Send

    @Serializable
    data class SendMessageRequest(
        val chat_id: String,
        val text: String,
        val parse_mode: String? = null,
        val disable_web_page_preview: Boolean? = null,
        val disable_notification: Boolean? = null,
        val reply_to_message_id: Int? = null,
        @Contextual val reply_markup: ReplyKeyboard? = null
    ) : TelegramRequest("sendMessage") {

        fun asUpdateBehaviour(
            name: String,
            editMessageTextRequest: EditMessageTextRequest,
            delay: Long = 0
        ): TelegramBehaviour.UpdateBehaviour =
            TelegramBehaviour.UpdateBehaviour(name,this, editMessageTextRequest, delay)
                
    }

    //Update
    @Serializable
    data class EditMessageTextRequest(
        val chat_id: String,
        val message_id: Int? = null,
        val inline_message_id: String? = null,
        val text: String,
        val parse_mode: String? = null,
        val disable_web_page_preview: Boolean? = null,
        val reply_markup: InlineKeyboardMarkup? = null
    ) : TelegramRequest("editMessageText")

    //Delete
    @Serializable
    data class DeleteMessageRequest(
        val chat_id: String,
        val message_id: Int
    ) : TelegramRequest("deleteMessage")
}

class TelegramMethod(
    botToken: String,
    isDebug: Boolean
) {
    private val client = TelegramHttpClient(botToken, isDebug)

    suspend fun <T> get(
        request: TelegramRequest,
        params: Map<String, Any> = mapOf(),
        isGetResult: Boolean = true,
        serializer: KSerializer<T>
    ): T {
        val json = client.getResponse(request, params).await()
        return Json { ignoreUnknownKeys = true }.decodeFromString(TelegramHttpClient.TelegramResponse.serializer(serializer), json).result
    }

    suspend fun execute(bodies: List<TelegramBehaviour>): List<Int> {
        return bodies.map { body -> client.sendMessageFromBehavior(body) }.awaitAll().map { it.result.message_id }
    }
    
}