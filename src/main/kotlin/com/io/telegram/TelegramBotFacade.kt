package com.io.telegram

import com.io.resourse.translateKeyboardMarkup
import com.io.util.inlineKeyBoardMarkup
import com.io.CommandConst
import com.io.StartMessage
import com.io.interactor.TelegramInteractor
import com.io.model.Language
import com.io.model.MessageGroup
import com.io.service.UserService

internal class TelegramBotFacade(
    private val userService: UserService,
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
            TelegramMessageHandler.Result.BehaviorForMessages.Translate -> translateMessage(chatId)
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

    private suspend fun translateMessage(chatId: String): (suspend (messageIds: List<Int>) -> Unit) {
        return {
            telegramInteractor.saveMessage(chatId, it)
        }
    }
}