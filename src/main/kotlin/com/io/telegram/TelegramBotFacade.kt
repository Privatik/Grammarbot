package com.io.telegram

import com.io.resourse.translateKeyboardMarkup
import com.io.service.keyBoardMarkup
import com.io.util.CommandConst
import com.io.util.StartMessage
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText

class TelegramBotFacade {

    private var currentLanguage = com.io.util.Message.Language.EN

    suspend fun handleUpdate(update: Update): BotApiMethod<out java.io.Serializable>?{
        if (update.hasCallbackQuery()){
            return handleCallbackQuery(update.callback_query!!)
        }

        if (update.hasMessage() && update.message!!.hasText()){
            return handleMessage(update.message)
        }
        return null
    }

    private suspend fun handleMessage(message: Message): SendMessage{
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

    private suspend fun handleCallbackQuery(callbackQuery: CallbackQuery): EditMessageText{
        if (callbackQuery.data == translateKeyboardMarkup.callbackData){
                currentLanguage = if (currentLanguage == com.io.util.Message.Language.EN){
                    com.io.util.Message.Language.RU
                } else {
                    com.io.util.Message.Language.EN
                }
            }

        return editMessageText(
            chat_id = callbackQuery.message!!.chat.id,
            text = StartMessage.get(currentLanguage),
            messageId = callbackQuery.message.message_id,
            replyMarkup = keyBoardMarkup()
        )
    }
}