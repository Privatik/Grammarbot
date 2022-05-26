package com.io.telegram

import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.TypeMessage
import com.io.resourse.CommandConst
import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.command.editTranslateMessage
import com.io.telegram.command.sendStartMessage
import com.io.util.GetListRViaFuncT
import com.io.util.extends.anotherLanguage
import com.io.util.extends.isSection

interface TelegramMessageHandler {
    suspend inline fun handleMessage(
        user: UserEntity,
        message: Message,
        messageIds: GetListRViaFuncT<com.io.cache.entity.MessageEntity, TypeMessage>
    ): List<Result>?

    suspend inline fun handleCallbackQuery(
        user: UserEntity,
        callbackQuery: CallbackQuery,
        messageIds: GetListRViaFuncT<com.io.cache.entity.MessageEntity, TypeMessage>
    ): List<Result>?

    data class Result(
        val chatId: String,
        val behaviour: TelegramBehaviour,
        val finishBehaviorUser: UserInteractor.BehaviorForUser,
        val finishBehaviorMessage: MessageInteractor.BehaviorForMessages
    )
}

internal class TelegramMessageHandlerImpl: TelegramMessageHandler {

    override suspend inline fun handleMessage(
        user: UserEntity,
        message: Message,
        messageIds: GetListRViaFuncT<MessageEntity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        handleCommandMessage(message, user, messageIds)?.let { result ->
            return result
        }

        return null
    }

    override suspend inline fun handleCallbackQuery(
        user: UserEntity,
        callbackQuery: CallbackQuery,
        messageIds: GetListRViaFuncT<MessageEntity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        handleCallbackQueryMessage(callbackQuery, user, messageIds)?.let { result ->
            return result
        }
        return null
    }

    private suspend inline fun handleCommandMessage(
        message: Message,
        user: UserEntity,
        messageIds: GetListRViaFuncT<MessageEntity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        return when (message.text){
            CommandConst.START -> sendStartMessage(message.chat.id, messageIds, user.currentLanguage)
            else -> null
        }
    }

    private suspend inline fun handleCallbackQueryMessage(
        callbackQuery: CallbackQuery,
        user: UserEntity,
        messageIds: GetListRViaFuncT<MessageEntity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        if (callbackQuery.data!!.isSection()){
            callbackQuery.message
        }
        return when (callbackQuery.data){
            translateKeyboardMarkup.callbackData -> editTranslateMessage(
                callbackQuery.message!!.chat.id,
                messageIds,
                user.anotherLanguage()
            )
            else -> null
        }
    }
}