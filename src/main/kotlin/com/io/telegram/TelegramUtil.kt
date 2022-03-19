package com.io.telegram

import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard

fun sendMessage(
    chat_id: String,
    text: String,
    parse_mode: String? = null,
    disable_web_page_preview: Boolean? = null,
    disable_notification: Boolean? = null,
    reply_to_message_id: Int? = null,
    replyMarkup: org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard? = null
):SendMessage =
    SendMessage(chat_id, text).apply {
        this.replyMarkup = replyMarkup
    }

fun editMessageText(
    chat_id: String,
    text: String,
    messageId: Int,
    replyMarkup: org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup? = null
):EditMessageText =
    EditMessageText().apply {
        this.chatId = chat_id
        this.text = text
        this.messageId = messageId
        this.replyMarkup = replyMarkup
    }