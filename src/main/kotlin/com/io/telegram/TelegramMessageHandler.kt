package com.io.telegram

import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.TypeMessage
import com.io.resourse.CommandConst
import com.io.resourse.Resourse
import com.io.resourse.translateKeyboardMarkup
import com.io.telegram.command.editTranslateMessage
import com.io.telegram.command.sendSectionMessage
import com.io.telegram.command.sendStartMessage
import com.io.util.GetListRViaFuncT
import com.io.util.extends.anotherLanguage
import com.io.util.extends.isSection

interface TelegramMessageHandler {
    suspend fun handleMessage(
        user: UserEntity,
        message: Message,
        messageIds: GetListRViaFuncT<com.io.cache.entity.MessageEntity, TypeMessage>
    ): List<Result>?

    suspend fun handleCallbackQuery(
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

    override suspend fun handleMessage(
        user: UserEntity,
        message: Message,
        messageIds: GetListRViaFuncT<com.io.cache.entity.MessageEntity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        handleCommandMessage(message, user, messageIds)?.let { result ->
            return result
        }

        return null
    }

    override suspend fun handleCallbackQuery(
        user: UserEntity,
        callbackQuery: CallbackQuery,
        messageIds: GetListRViaFuncT<com.io.cache.entity.MessageEntity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        handleCallbackQueryMessage(callbackQuery, user, messageIds)?.let { result ->
            return result
        }
        return null
    }

    private suspend fun handleCommandMessage(
        message: Message,
        user: UserEntity,
        messageIds: GetListRViaFuncT<com.io.cache.entity.MessageEntity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        return when (message.text){
            CommandConst.START -> sendStartMessage(message.chat.id, message.message_id, messageIds, user.currentLanguage)
            else -> null
        }
    }

    private suspend fun handleCallbackQueryMessage(
        callbackQuery: CallbackQuery,
        user: UserEntity,
        messageIds: GetListRViaFuncT<com.io.cache.entity.MessageEntity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        if (callbackQuery.data!!.isSection()){
            return sendSectionMessage(
                chatId = callbackQuery.message!!.chat.id,
                sectionId = callbackQuery.data!!.drop(Resourse.section.length),
                messageIds = messageIds,
                language = user.currentLanguage
            )
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