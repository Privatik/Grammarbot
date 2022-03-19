package com.io.telegram

import com.io.resourse.translateKeyboardMarkup
import com.io.util.keyBoardMarkup
import com.io.CommandConst
import com.io.StartMessage

class TelegramBotFacade {

    private var currentLanguage = com.io.Message.Language.EN

    suspend fun handleUpdate(update: Update): TelegramRequest?{
        if (update.hasCallbackQuery()){
            return handleCallbackQuery(update.callback_query!!)
        }

        if (update.hasMessage() && update.message!!.hasText()){
            return handleMessage(update.message!!)
        }
        return null
    }

    private suspend fun handleMessage(message: Message): TelegramRequest.SendMessageRequest{
        val replyMessage = when (message.text){
            CommandConst.START -> StartMessage.get(currentLanguage)
            else -> "Hi ${message.text}"
        }

        val replyMarkup = if (message.text == CommandConst.START) keyBoardMarkup() else null

        return sendMessage(
            chat_id = message.chat.id,
            text = replyMessage,
            replyMarkup = replyMarkup
        )
    }

    private suspend fun handleCallbackQuery(callbackQuery: CallbackQuery): TelegramRequest.EditMessageTextRequest{
        if (callbackQuery.data == translateKeyboardMarkup.callbackData){
                currentLanguage = if (currentLanguage == com.io.Message.Language.EN){
                    com.io.Message.Language.RU
                } else {
                    com.io.Message.Language.EN
                }
            }

        return editMessageText(
            chat_id = callbackQuery.message!!.chat.id,
            text = StartMessage.get(currentLanguage),
            messageId = callbackQuery.message!!.message_id,
            replyMarkup = keyBoardMarkup()
        )
    }
}