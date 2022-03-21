package com.io.telegram

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.serialization.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable

@Serializable
sealed class TelegramRequest(val path: String){

    //Setting

    @Serializable
    data class SetWebhookRequest(
        val url: String,
    ) : TelegramRequest("setWebhook")

    //Send

    @Serializable
    data class SendMessageRequest(
        val chat_id: String,
        val text: String,
        val parse_mode: String? = null,
        val disable_web_page_preview: Boolean? = null,
        val disable_notification: Boolean? = null,
        val reply_to_message_id: Int? = null,
        val reply_markup: ReplyKeyboard? = null
    ) : TelegramRequest("sendMessage")

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
    botToken: String
) {
    private val basePath = "https://api.telegram.org/bot${botToken}"
    private val client = HttpClient(CIO){}

    suspend fun execute(body: TelegramRequest) = withContext(Dispatchers.IO) {
        println("Send nessage $body")
        client.post<HttpResponse>("$basePath/${body.path}") {
            this.body = body
            contentType(ContentType.Application.Json)
        }
        //client.close()
        Unit
    }
}