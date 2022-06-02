package com.io.telegram

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.TypeMessage
import com.io.resourse.CommandConst
import com.io.resourse.Resourse
import com.io.telegram.command.editTranslateMessage
import com.io.telegram.command.sendSectionMessage
import com.io.telegram.command.sendStartMessage
import com.io.telegram.command.stepBack
import com.io.util.GetListRViaFuncT
import com.io.util.extends.anotherLanguage
import com.io.util.extends.isSection

interface TelegramMessageHandler {
    suspend fun handleMessage(
        user: UserEntity,
        message: Message,
        messageIds: GetListRViaFuncT<Entity, TypeMessage>
    ): List<Result>?

    suspend fun handleCallbackQuery(
        user: UserEntity,
        callbackQuery: CallbackQuery,
        messageIds: GetListRViaFuncT<Entity, TypeMessage>
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
        messageIds: GetListRViaFuncT<Entity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        handleCommandMessage(message, user, messageIds)?.let { result ->
            return result
        }

        return null
    }

    override suspend fun handleCallbackQuery(
        user: UserEntity,
        callbackQuery: CallbackQuery,
        messageIds: GetListRViaFuncT<Entity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        handleCallbackQueryMessage(callbackQuery, user, messageIds)?.let { result ->
            return result
        }
        return null
    }

    private suspend fun handleCommandMessage(
        message: Message,
        user: UserEntity,
        messageIds: GetListRViaFuncT<Entity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        return when (message.text){
            CommandConst.START -> sendStartMessage(user, message.message_id, messageIds)
            else -> null
        }
    }

    private suspend fun handleCallbackQueryMessage(
        callbackQuery: CallbackQuery,
        user: UserEntity,
        messageIds: GetListRViaFuncT<Entity, TypeMessage>
    ): List<TelegramMessageHandler.Result>? {
        if (callbackQuery.data!!.isSection()){
            return sendSectionMessage(
                userEntity = user,
                sectionId = callbackQuery.data!!.drop(Resourse.section.length),
                messageIds = messageIds,
            )
        }
        return when (callbackQuery.data){
            com.io.resourse.Message.StartLessonMessage.callBack -> stepBack(
                user,
                messageIds
            )
            com.io.resourse.Message.BackLessonMessage.callBack -> stepBack(
                user,
                messageIds
            )
            com.io.resourse.Message.TranslateMessage.callBack -> editTranslateMessage(
                user,
                messageIds,
                user.anotherLanguage()
            )
            else -> null
        }
    }
}