package com.io.telegram

import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.interactor.TelegramInteractor
import com.io.interactor.UserInteractor
import com.io.model.MessageGroup
import com.io.model.asMessageGroup

internal class TelegramBotFacade(
    private val telegramInteractor: TelegramInteractor<
            (suspend (messageIds: Pair<Int, MessageGroup>) -> MessageEntity?),
            (suspend () -> UserEntity?)
            >,
    private val telegramMessageHandler: TelegramMessageHandler
) {

    suspend fun handleUpdate(update: Update): List<TelegramResult>? {
        val currentUser: UserEntity?
        if (update.hasCallbackQuery()){
            currentUser = getUser(update.callback_query!!.message!!.chat.id)
            val messageIds = telegramInteractor.getMessagesAsMapByMessageGroup(update.callback_query!!.message!!.chat.id)
            return telegramMessageHandler.handleCallbackQuery(currentUser, update.callback_query!!, messageIds)?.asTelegramResults()
        }

        if (update.hasMessage() && update.message!!.hasText()){
            currentUser = getUser(update.message!!.chat.id)
            val messageIds = telegramInteractor.getMessagesAsMapByMessageGroup(update.message!!.chat.id)
            return telegramMessageHandler.handleMessage(currentUser, update.message!!, messageIds)?.asTelegramResults()
        }

        return null
    }

    private suspend fun List<TelegramMessageHandler.Result>.asTelegramResults(): List<TelegramResult>{
        return map { result ->
            val methodForMessage = telegramInteractor.processingMessage(result.chatId, result.finishBehaviorMessage)
            val methodForUser = telegramInteractor.processingUser(result.chatId, result.finishBehaviorUser)

            TelegramResult(
                behaviour = result.behaviour,
                doFinish = {
                    val newValue = it.first to it.second.asMessageGroup()
                    methodForMessage(newValue)
                    methodForUser()
                }
            )
        }
    }

    private suspend fun processingCallbackQuery(
        user: UserEntity,
        messageIds: suspend ( suspend (com.io.cache.entity.MessageEntity) -> Boolean) -> Map<MessageGroup, List<Int>>
    ): List<TelegramMessageHandler.Result>? {
        return telegramMessageHandler.handleCallbackQuery(user, update.callback_query!!, messageIds)
    }

    private suspend fun processingMessage(
        user: UserEntity,
        messageIds: suspend ( suspend (com.io.cache.entity.MessageEntity) -> Boolean) -> Map<MessageGroup, List<Int>>
    ): List<TelegramMessageHandler.Result>? {
        return telegramMessageHandler.handleCallbackQuery(user, update.callback_query!!, messageIds)
    }

    private suspend fun getUser(chatId: String): UserEntity{
        return telegramInteractor.processingUser(chatId, UserInteractor.BehaviorForUser.GetOrCreate)()!!
    }

    private suspend fun processingUpdate(chatId: String){
        val currentUser = getUser(chatId)
        val messageIds = telegramInteractor.getMessagesAsMapByMessageGroup(chatId)
    }
}