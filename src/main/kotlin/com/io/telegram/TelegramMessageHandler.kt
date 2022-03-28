package com.io.telegram

import com.io.resourse.CommandConst
import com.io.resourse.StartMessage
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.command.editStartMessage
import com.io.telegram.command.sendStartMessage
import com.io.util.inlineKeyBoardMarkup

internal interface TelegramMessageHandler {
    suspend fun handleMessage(message: Message): Result?
    suspend fun handleCallbackQuery(callbackQuery: CallbackQuery, messageIds: Map<String, Int>): Result?

    data class Result(
        val chatId: String,
        val behaviours: List<TelegramBehaviour>,
        val finishBehavior: BehaviorForMessages
    ){
        sealed class BehaviorForMessages {
            object Save: BehaviorForMessages()
            object Delete: BehaviorForMessages()
            object None: BehaviorForMessages()
        }
    }
}

internal class TelegramMessageHandlerImpl: TelegramMessageHandler {

    var currentLanguage = Language.EN

    override suspend fun handleMessage(message: Message): TelegramMessageHandler.Result{
        val messages = mutableListOf<TelegramBehaviour>()

        handleCommandMessage(message)?.let { command ->
            return TelegramMessageHandler.Result(
                chatId = message.chat.id,
                behaviours = command,
                finishBehavior = TelegramMessageHandler.Result.BehaviorForMessages.Save
            )
        }

        val replyMessage = "Hi ${message.text}"

        val sendMessage = sendMessage(
            chat_id = message.chat.id,
            text = replyMessage,
            replyMarkup = ReplyKeyboardRemove(true)
        )

        messages.add(
            sendMessage.asSendBehaviour(MessageGroup::class.java.name)
        )

        return TelegramMessageHandler.Result(
            chatId = message.chat.id,
            behaviours = messages,
            finishBehavior = TelegramMessageHandler.Result.BehaviorForMessages.Save
        )
    }

    private fun handleCommandMessage(message: Message): List<TelegramBehaviour>? {
        return when (message.text){
            CommandConst.START -> sendStartMessage(message.chat.id, currentLanguage)
            else -> null
        }
    }

    private fun handleCallbackQueryMessage(callbackQuery: CallbackQuery, messageIds: Map<String,Int>): List<TelegramBehaviour>? {
        return when (callbackQuery.data){
            translateKeyboardMarkup.callbackData -> editStartMessage(
                callbackQuery.message!!.chat.id,
                messageIds,
                currentLanguage
            )
            else -> null
        }
    }

    override suspend fun handleCallbackQuery(
        callbackQuery: CallbackQuery,
        messageIds: Map<String,Int>
    ): TelegramMessageHandler.Result {
        val messages = mutableListOf<TelegramBehaviour>()

        when (callbackQuery.data){
            translateKeyboardMarkup.callbackData -> {
                currentLanguage = if (currentLanguage == Language.EN){
                    Language.RU
                } else {
                    Language.EN
                }
            }
        }

        handleCallbackQueryMessage(callbackQuery, messageIds)?.let { command ->
            return TelegramMessageHandler.Result(
                chatId = callbackQuery.message!!.chat.id,
                behaviours = command,
                finishBehavior = TelegramMessageHandler.Result.BehaviorForMessages.Save
            )
        }

        messages.add(
            TelegramBehaviour.Send(
                cName = "edit",
                request = editMessageText(
                    chat_id = callbackQuery.message!!.chat.id,
                    text = StartMessage.get(currentLanguage),
                    messageId = callbackQuery.message!!.message_id,
                    replyMarkup = inlineKeyBoardMarkup(currentLanguage)
                )
            )
        )

        return TelegramMessageHandler.Result(
            chatId = callbackQuery.message!!.chat.id,
            behaviours = messages,
            finishBehavior = TelegramMessageHandler.Result.BehaviorForMessages.Save
        )
    }
}