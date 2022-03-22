package com.io.telegram

import com.io.resourse.translateKeyboardMarkup
import com.io.util.inlineKeyBoardMarkup
import com.io.CommandConst
import com.io.StartMessage
import com.io.model.Language
import com.io.service.UserService

class TelegramBotFacade(
    private val userService: UserService
) {
    private var currentLanguage = Language.EN

    suspend fun handleUpdate(update: Update): Pair<String,List<TelegramBehaviour>>? {
        if (update.hasCallbackQuery()){
            return Pair(update.callback_query!!.message!!.chat.id,handleCallbackQuery(update.callback_query!!))
        }

        if (update.hasMessage() && update.message!!.hasText()){
            return Pair(update.message!!.chat.id,handleMessage(update.message!!))
        }
        return null
    }

    private suspend fun handleMessage(message: Message): List<TelegramBehaviour>{
        val messages = mutableListOf<TelegramBehaviour>()

        val replyMessage = when (message.text){
            CommandConst.START -> return getStartMessages(message, currentLanguage)
            else -> "Hi ${message.text}"
        }

        val sendMessage = sendMessage(
                chat_id = message.chat.id,
                text = replyMessage,
                replyMarkup = ReplyKeyboardRemove(true)
            )

        messages.add(
            sendMessage.asSendBehaviour()
        )

        return messages
    }

    private suspend fun handleCallbackQuery(callbackQuery: CallbackQuery): List<TelegramBehaviour>{
        val messages = mutableListOf<TelegramBehaviour>()

        val neMessages = when (callbackQuery.data){
            translateKeyboardMarkup.callbackData -> {
                currentLanguage = if (currentLanguage == Language.EN){
                    Language.RU
                } else {
                    Language.EN
                }

                userService.getMessageIds(callbackQuery.message!!.chat.id){true}
            }
            else -> throw Exception("Not find callback")
        }

        messages.add(
            TelegramBehaviour.Send(
                editMessageText(
                    chat_id = callbackQuery.message!!.chat.id,
                    text = StartMessage.get(currentLanguage),
                    messageId = callbackQuery.message!!.message_id,
                    replyMarkup = inlineKeyBoardMarkup(currentLanguage)
                )
            )
        )

        return messages
    }
}