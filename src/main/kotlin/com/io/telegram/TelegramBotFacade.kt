package com.io.telegram

import com.io.interactor.TelegramInteractor
import com.io.model.MessageGroup
import com.io.cache.entity.UserEntity

internal class TelegramBotFacade(
    private val telegramInteractor: TelegramInteractor,
    private val telegramMessageHandler: TelegramMessageHandler
) {

    suspend fun handleUpdate(update: Update): TelegramResult? {
        val currentUser: UserEntity?
        if (update.hasCallbackQuery()){
            currentUser = telegramInteractor.getOrSaveNewUser(update.callback_query!!.message!!.chat.id)
            val messageIds = telegramInteractor.getMessage(update.callback_query!!.message!!.chat.id) {
                it.group == MessageGroup.START.name || it.group == MessageGroup.CHOICE_SECTION.name
            }
            return telegramMessageHandler.handleCallbackQuery(currentUser, update.callback_query!!, messageIds)?.asTelegramResult()
        }

        if (update.hasMessage() && update.message!!.hasText()){
            currentUser = telegramInteractor.getOrSaveNewUser(update.message!!.chat.id)
            val messageIds = telegramInteractor.getMessage(update.message!!.chat.id) {
                it.group == MessageGroup.START.name || it.group == MessageGroup.CHOICE_SECTION.name
            }
            return telegramMessageHandler.handleMessage(currentUser, update.message!!, messageIds)?.asTelegramResults()
        }

        return null
    }

    private suspend fun List<TelegramMessageHandler.Result>.asTelegramResults(): List<TelegramResult>{
        return map { result ->
            val method: suspend (messageIds: Pair<String,Int>) -> Unit = when (result.finishBehaviorMessage){
                TelegramMessageHandler.Result.BehaviorForMessages.Save -> saveMessage(result.chatId)
                TelegramMessageHandler.Result.BehaviorForMessages.Delete -> { _ : Pair<String,Int> -> }
                TelegramMessageHandler.Result.BehaviorForMessages.None -> { _ : Pair<String,Int> -> }
            }

            when (result.finishBehaviorUser){
                is TelegramMessageHandler.Result.BehaviorForUser.Update -> {
                    telegramInteractor.updateUser(
                        chatId = result.chatId,
                        language = result.finishBehaviorUser.language,
                        state = result.finishBehaviorUser.state
                    )
                }
                is TelegramMessageHandler.Result.BehaviorForUser.None -> Unit
            }

            TelegramResult(
                behaviour = result.behaviour,
                doFinish = method
            )
        }
    }

    private suspend fun saveMessage(chatId: String): (suspend (messageIds: Pair<String,Int>) -> Unit) {
        return {
            telegramInteractor.saveMessage(chatId, it)
        }
    }
}