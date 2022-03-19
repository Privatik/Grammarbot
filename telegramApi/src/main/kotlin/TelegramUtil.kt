package com.io.telegram

fun sendMessage(
    chat_id: String,
    text: String,
    parse_mode: String? = null,
    disable_web_page_preview: Boolean? = null,
    disable_notification: Boolean? = null,
    reply_to_message_id: Int? = null,
    replyMarkup: ReplyKeyboard? = null
):TelegramRequest.SendMessageRequest =
    TelegramRequest.SendMessageRequest(
        chat_id = chat_id,
        text = text,
        reply_markup = replyMarkup
    )

fun editMessageText(
    chat_id: String,
    text: String,
    messageId: Int,
    replyMarkup: InlineKeyboardMarkup? = null
): TelegramRequest.EditMessageTextRequest =
    TelegramRequest.EditMessageTextRequest(
        chat_id = chat_id,
        text = text,
        message_id = messageId,
        reply_markup = replyMarkup
    )

fun deleteMessage(
    chat_id: String,
    messageId: Int
): TelegramRequest.DeleteMessageRequest =
    TelegramRequest.DeleteMessageRequest(
        chat_id = chat_id,
        message_id = messageId
    )