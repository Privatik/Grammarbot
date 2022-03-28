package com.io.telegram

import com.io.cache.entity.UserEntity
import com.io.resourse.CommandConst
import com.io.resourse.StartMessage
import com.io.model.Language
import com.io.model.UserState
import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.command.editStartMessage
import com.io.telegram.command.sendStartMessage
import com.io.util.extends.anotherLanguage
import com.io.util.inlineKeyBoardMarkup

internal interface TelegramMessageHandler {
    suspend fun handleMessage(user: UserEntity, message: Message): Result?
    suspend fun handleCallbackQuery(
        user: UserEntity,
        callbackQuery: CallbackQuery,
        messageIds: Map<String,List<Int>>,
    ): Result?

    data class Result(
        val chatId: String,
        val behaviours: List<TelegramBehaviour>,
        val finishBehaviorUser: BehaviorForUser,
        val finishBehaviorMessage: BehaviorForMessages
    ){
        sealed class BehaviorForUser {
            data class Update(
                val language: Language? = null,
                val state: UserState? = null
            ): BehaviorForUser()
            object None: BehaviorForUser()
        }

        sealed class BehaviorForMessages {
            object Save: BehaviorForMessages()
            object Delete: BehaviorForMessages()
            object None: BehaviorForMessages()
        }
    }
}

internal class TelegramMessageHandlerImpl: TelegramMessageHandler {

    override suspend fun handleMessage(user: UserEntity, message: Message): TelegramMessageHandler.Result?{
        handleCommandMessage(message, user)?.let { result ->
            return result
        }

        return null
    }

    override suspend fun handleCallbackQuery(
        user: UserEntity,
        callbackQuery: CallbackQuery,
        messageIds: Map<String,List<Int>>
    ): TelegramMessageHandler.Result? {
        handleCallbackQueryMessage(callbackQuery, user, messageIds)?.let { result ->
            return result
        }
        return null
    }

    private fun handleCommandMessage(message: Message, user: UserEntity): TelegramMessageHandler.Result? {
        return when (message.text){
            CommandConst.START -> {
                TelegramMessageHandler.Result(
                    chatId = message.chat.id,
                    behaviours = sendStartMessage(message.chat.id, user.currentLanguage),
                    finishBehaviorUser = TelegramMessageHandler.Result.BehaviorForUser.None,
                    finishBehaviorMessage = TelegramMessageHandler.Result.BehaviorForMessages.Save
                )
            }
            else -> null
        }
    }

    private fun handleCallbackQueryMessage(callbackQuery: CallbackQuery, user: UserEntity, messageIds: Map<String, List<Int>>): TelegramMessageHandler.Result? {
        return when (callbackQuery.data){
            translateKeyboardMarkup.callbackData ->
                TelegramMessageHandler.Result(
                    chatId = callbackQuery.message!!.chat.id,
                    behaviours = editStartMessage(
                        callbackQuery.message!!.chat.id,
                        messageIds,
                        user.anotherLanguage()
                    ),
                    finishBehaviorUser = TelegramMessageHandler.Result.BehaviorForUser.Update(language = user.anotherLanguage()),
                    finishBehaviorMessage = TelegramMessageHandler.Result.BehaviorForMessages.Save
                )
            else -> null
        }
    }
}