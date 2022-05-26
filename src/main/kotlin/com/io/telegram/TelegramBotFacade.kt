package com.io.telegram

import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.interactor.TelegramInteractor
import com.io.interactor.UserInteractor
import com.io.model.MessageGroup
import com.io.model.asMessageGroup
import com.io.util.*

class TelegramBotFacade(
    private val telegramInteractor: TelegramInteractor<GetMessageEntityViaIntToMessageGroup, GetUserEntity>,
    private val telegramMessageHandler: TelegramMessageHandler
) {

    suspend fun handleUpdate(update: Update): List<TelegramResult>? {
        if (update.hasCallbackQuery()){
            val userToIds = telegramInteractor.getUserToMessageIds(update.callback_query!!.message!!.chat.id)
            return telegramMessageHandler.handleCallbackQuery(
                userToIds.first,
                update.callback_query!!,
                userToIds.second
            )?.asTelegramResults()
        }

        if (update.hasMessage() && update.message!!.hasText()){
            val userToIds = telegramInteractor.getUserToMessageIds(update.message!!.chat.id)
            return telegramMessageHandler.handleMessage(
                userToIds.first,
                update.message!!,
                userToIds.second
            )?.asTelegramResults()
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
                    methodForMessage(it.first, it.second.asMessageGroup())
                    methodForUser()
                }
            )
        }
    }
}