package com.io.telegram

import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.MessageGroup
import com.io.resourse.CommandConst
import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.command.editStartMessage
import com.io.telegram.command.sendStartMessage
import com.io.util.extends.anotherLanguage

internal interface TelegramMessageHandler {
    suspend fun handleMessage(
        user: UserEntity,
        message: Message,
        messageIds: suspend ( suspend (com.io.cache.entity.MessageEntity) -> Boolean) -> Map<MessageGroup, List<Int>>
    ): List<Result>?

    suspend fun handleCallbackQuery(
        user: UserEntity,
        callbackQuery: CallbackQuery,
        messageIds: suspend ( suspend (com.io.cache.entity.MessageEntity) -> Boolean) -> Map<MessageGroup, List<Int>>,
    ): List<Result>?

    data class Result(
        val chatId: String,
        val behaviour: TelegramBehaviour,
        val finishBehaviorUser: UserInteractor.BehaviorForUser,
        val finishBehaviorMessage: MessageInteractor.BehaviorForMessages
    )
}

internal class TelegramMessageHandlerImpl: TelegramMessageHandler {

    override suspend fun handleMessage(
        user: UserEntity,
        message: Message,
        messageIds: suspend ( suspend (com.io.cache.entity.MessageEntity) -> Boolean) -> Map<MessageGroup, List<Int>>
    ): List<TelegramMessageHandler.Result>? {
        handleCommandMessage(message, user, messageIds)?.let { result ->
            return result
        }

        return null
    }

    override suspend fun handleCallbackQuery(
        user: UserEntity,
        callbackQuery: CallbackQuery,
        messageIds: suspend ( suspend (com.io.cache.entity.MessageEntity) -> Boolean) -> Map<MessageGroup, List<Int>>
    ): List<TelegramMessageHandler.Result>? {
        handleCallbackQueryMessage(callbackQuery, user, messageIds)?.let { result ->
            return result
        }
        return null
    }

    private suspend fun handleCommandMessage(
        message: Message,
        user: UserEntity,
        messageIds: suspend ( suspend (com.io.cache.entity.MessageEntity) -> Boolean) -> Map<MessageGroup, List<Int>>
    ): List<TelegramMessageHandler.Result>? {
        return when (message.text){
            CommandConst.START -> sendStartMessage(message.chat.id, messageIds, user.currentLanguage)
            else -> null
        }
    }

    private suspend fun handleCallbackQueryMessage(
        callbackQuery: CallbackQuery,
        user: UserEntity,
        messageIds: suspend ( suspend (com.io.cache.entity.MessageEntity) -> Boolean) -> Map<MessageGroup, List<Int>>
    ): List<TelegramMessageHandler.Result>? {
        return when (callbackQuery.data){
            translateKeyboardMarkup.callbackData -> editStartMessage(
                callbackQuery.message!!.chat.id,
                messageIds,
                user.anotherLanguage()
            )
            else -> null
        }
    }
}