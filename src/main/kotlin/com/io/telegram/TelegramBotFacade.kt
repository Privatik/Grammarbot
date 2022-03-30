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
            return telegramMessageHandler.handleMessage(currentUser, update.message!!, messageIds)?.asTelegramResult()
        }

        return null
    }

    private suspend fun TelegramMessageHandler.Result.asTelegramResult(): TelegramResult{
        val method: suspend (messageIds: List<Pair<String,Int>>) -> Unit = when (finishBehaviorMessage){
            TelegramMessageHandler.Result.BehaviorForMessages.Save -> saveMessage(chatId)
            TelegramMessageHandler.Result.BehaviorForMessages.Delete -> { _ : List<Pair<String,Int>> -> }
            TelegramMessageHandler.Result.BehaviorForMessages.None -> { _ : List<Pair<String,Int>> -> }
        }

        when (finishBehaviorUser){
            is TelegramMessageHandler.Result.BehaviorForUser.Update -> { telegramInteractor.updateUser(
                chatId = chatId,
                language = finishBehaviorUser.language,
                state = finishBehaviorUser.state
            )}
            is TelegramMessageHandler.Result.BehaviorForUser.None -> Unit
        }

        return TelegramResult(
            behaviours = behaviours,
            doFinish = method
        )
    }

    private suspend fun saveMessage(chatId: String): (suspend (messageIds: List<Pair<String,Int>>) -> Unit) {
        return {
            telegramInteractor.saveMessage(chatId, it)
        }
    }
}