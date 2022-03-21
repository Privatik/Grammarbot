package com.io.telegram

import com.io.resourse.translateKeyboardMarkup
import com.io.service.inlineKeyBoardMarkup
import com.io.CommandConst
import com.io.StartMessage
import com.io.model.Language
import com.io.service.replyKeyBoardMarkup

class TelegramBotFacade {

    private var currentLanguage = Language.EN

    suspend fun handleUpdate(update: Update): List<TelegramRequest>? {
        if (update.hasCallbackQuery()){
            return handleCallbackQuery(update.callback_query!!)
        }

        if (update.hasMessage() && update.message!!.hasText()){
            return handleMessage(update.message!!)
        }
        return null
    }

    private suspend fun handleMessage(message: Message): List<TelegramRequest>{
        val messages = mutableListOf<TelegramRequest>()

        val replyMessage = when (message.text){
            CommandConst.START -> StartMessage.get(currentLanguage)
            else -> "Hi ${message.text}"
        }

        val replyMarkup = if (message.text == CommandConst.START) replyKeyBoardMarkup(currentLanguage) else null

        messages.add(
            sendMessage(
                chat_id = message.chat.id,
                text = replyMessage,
                replyMarkup = replyMarkup
            )
        )

        if (message.text == CommandConst.START){
            messages.add(
                editMessageText(
                    chat_id = message.chat.id,
                    text = replyMessage,
                    messageId = message.message_id,
                    replyMarkup = inlineKeyBoardMarkup(currentLanguage)
                )
            )
        }

        return messages
    }

    private suspend fun handleCallbackQuery(callbackQuery: CallbackQuery): List<TelegramRequest>{
        val messages = mutableListOf<TelegramRequest>()

        if (callbackQuery.data == translateKeyboardMarkup.callbackData){
                currentLanguage = if (currentLanguage == Language.EN){
                    Language.RU
                } else {
                    Language.EN
                }
            }

        messages.add(
            editMessageText(
                chat_id = callbackQuery.message!!.chat.id,
                text = StartMessage.get(currentLanguage),
                messageId = callbackQuery.message!!.message_id,
                replyMarkup = inlineKeyBoardMarkup(currentLanguage)
            )
        )

        return messages
    }
}