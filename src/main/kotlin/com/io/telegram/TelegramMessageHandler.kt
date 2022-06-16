package com.io.telegram

import com.io.cache.entity.Entity
import com.io.cache.entity.UserEntity
import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.model.LessonState
import com.io.model.MultiBehaviours
import com.io.model.TypeMessage
import com.io.model.UserState
import com.io.resourse.CommandConst
import com.io.resourse.Message.Companion.FinishLessonMessage
import com.io.resourse.Resourse
import com.io.telegram.command.*
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

    sealed interface Result{
        data class Ordinary(
            val chatId: String,
            val behaviour: TelegramBehaviour,
            val finishBehaviorUser: UserInteractor.BehaviorForUser,
            val finishBehaviorMessage: MessageInteractor.BehaviorForMessages
        ): Result

        data class Order(
            val behaviour: Ordinary,
            val behaviours: List<MultiBehaviours>,
        ): Result
    }


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

        when (user.currentState){
            UserState.LEARN -> {
                return sendAnswerTaskMessage(user, messageIds)
            }
            UserState.PAUSE_LEARN -> {
                return deleteLastMessage(user, message.message_id)
            }
            else -> {}
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
        val text = message.text!!
        return when {
            text == CommandConst.START -> sendStartMessage(user, message.message_id, messageIds)
            FinishLessonMessage.checkOnEqualsByLanguageField(text) -> {
                null
            }
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
            com.io.resourse.Message.PutTaskMessage.callBack -> sendTaskMessage(
                user,
                messageIds,
                LessonState.PUT
            )
            com.io.resourse.Message.WriteTaskMessage.callBack -> sendTaskMessage(
                user,
                messageIds,
                LessonState.WRITE
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
            com.io.resourse.Message.NextMessage.callBack -> sendTaskMessage(
                user,
                messageIds
            )
            else -> null
        }
    }
}