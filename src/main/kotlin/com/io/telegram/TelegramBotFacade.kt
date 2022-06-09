package com.io.telegram

import com.io.interactor.TelegramInteractor
import com.io.model.asMessageGroup
import com.io.util.GetMessageEntityViaIntToMessageGroup
import com.io.util.GetUserEntity
import com.io.util.extends.getUserToMessageIds

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
            when (result){
                is TelegramMessageHandler.Result.Delay -> result.asTelegramResult()
                is TelegramMessageHandler.Result.Ordinary -> result.asTelegramResult()
            }

        }
    }

    private suspend fun TelegramMessageHandler.Result.Ordinary.asTelegramResult(): TelegramResult.Ordinary {
        val methodForMessage = telegramInteractor.processingMessage(chatId, finishBehaviorMessage)
        val methodForUser = telegramInteractor.processingUser(chatId, finishBehaviorUser)

        return TelegramResult.Ordinary(
            behaviour = behaviour,
            doFinish = {
                methodForMessage(it.first, it.second.asMessageGroup())
                methodForUser()
            }
        )
    }

    private suspend fun TelegramMessageHandler.Result.Delay.asTelegramResult(): TelegramResult.Delay {
        val ordinary = behaviour.asTelegramResult()

        return TelegramResult.Delay(
            behaviour = ordinary,
            behaviours = behaviours.map { getResultOrdinary ->
                { id -> getResultOrdinary(id).asTelegramResult() }
            }
        )
    }
}