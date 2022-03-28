package com.io.telegram

import com.io.interactor.TelegramInteractor
import com.io.model.MessageGroup
import com.io.cache.UserCache

internal class TelegramBotFacade(
    private val userCache: UserCache,
    private val telegramInteractor: TelegramInteractor,
    private val telegramMessageHandler: TelegramMessageHandler
) {

    suspend fun handleUpdate(update: Update): TelegramResult? {
        if (update.hasCallbackQuery()){
            val messageIds = telegramInteractor.getMessage(update.callback_query!!.message!!.chat.id) {
                group == MessageGroup.START || group == MessageGroup.CHOICE_SECTION
            }
            return telegramMessageHandler.handleCallbackQuery(update.callback_query!!, messageIds)?.asTelegramResult()
        }

        if (update.hasMessage() && update.message!!.hasText()){
            return telegramMessageHandler.handleMessage(update.message!!)?.asTelegramResult()
        }

        return null
    }

    private suspend fun TelegramMessageHandler.Result.asTelegramResult(): TelegramResult{
        val method: suspend (messageIds: List<Int>) -> Unit = when (finishBehavior){
            TelegramMessageHandler.Result.BehaviorForMessages.Save -> saveMessage(chatId)
            TelegramMessageHandler.Result.BehaviorForMessages.Delete -> { _ : List<Int> -> }
            TelegramMessageHandler.Result.BehaviorForMessages.None -> { _ : List<Int> -> }
        }

        return TelegramResult(
            behaviours = behaviours,
            doFinish = method
        )
    }

    private suspend fun saveMessage(chatId: String): (suspend (messageIds: List<Int>) -> Unit) {
        return {
            telegramInteractor.saveMessage(chatId, it)
        }
    }
}