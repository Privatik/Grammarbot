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
                is TelegramMessageHandler.Result.Order -> result.asTelegramResult()
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

    private suspend fun TelegramMessageHandler.Result.Order.asTelegramResult(): TelegramResult.Order {
        val ordinary = behaviour.asTelegramResult()

        fun ((Int) -> TelegramBehaviour).asBody(): suspend (Int) -> TelegramResult.Ordinary {
            return {
                TelegramResult.Ordinary(
                    behaviour = this(it),
                    doFinish = { }
                )
            }
        }

        val orderBehaviours = behaviours.map {
            it.behaviour.asBody() to it.name
        }

        val doFinish: suspend ( List<Pair<Int, String>>) -> Unit = {
            it.forEachIndexed { index, pair ->
                if (index == 0){
                    ordinary.doFinish(pair)
                } else {
                    telegramInteractor.processingMessage(behaviour.chatId, behaviours[index - 1].finishBehaviorMessage)
                        .apply {
                            this(pair.first, pair.second.asMessageGroup())
                        }
                    telegramInteractor.processingUser(behaviour.chatId, behaviours[index - 1].finishBehaviorUser)
                        .apply {
                            this()
                        }
                }
            }
        }

        return TelegramResult.Order(
            behaviour = ordinary,
            behaviours = orderBehaviours,
            doFinish = doFinish
        )
    }
}