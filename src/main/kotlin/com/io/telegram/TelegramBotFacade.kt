package com.io.telegram

import com.io.interactor.TelegramInteractor
import com.io.model.MessageGroup
import com.io.cache.entity.UserEntity

internal class TelegramBotFacade(
    private val telegramInteractor: TelegramInteractor,
    private val telegramMessageHandler: TelegramMessageHandler
) {

    suspend fun handleUpdate(update: Update): List<TelegramResult>? {
        val currentUser: UserEntity?
        if (update.hasCallbackQuery()){
            currentUser = telegramInteractor.getOrSaveNewUser(update.callback_query!!.message!!.chat.id)
            val messageIds = telegramInteractor.getMessage(update.callback_query!!.message!!.chat.id) {
                it.group == MessageGroup.START.name || it.group == MessageGroup.CHOICE_SECTION.name
            }
            return telegramMessageHandler.handleCallbackQuery(currentUser, update.callback_query!!, messageIds)?.asTelegramResults()
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
            val methodForMessage: suspend (messageIds: Pair<String,Int>) -> Unit = when (result.finishBehaviorMessage){
                TelegramMessageHandler.Result.BehaviorForMessages.Save -> saveMessage(result.chatId)
                TelegramMessageHandler.Result.BehaviorForMessages.Delete -> { _ : Pair<String,Int> -> }
                TelegramMessageHandler.Result.BehaviorForMessages.None -> { _ : Pair<String,Int> -> }
            }

            val methodForUser: suspend () -> Unit = when (result.finishBehaviorUser){
                is TelegramMessageHandler.Result.BehaviorForUser.Update -> {
                    {
                        telegramInteractor.updateUser(
                            chatId = result.chatId,
                            language = result.finishBehaviorUser.language,
                            state = result.finishBehaviorUser.state
                        )
                    }
                }
                is TelegramMessageHandler.Result.BehaviorForUser.None -> {
                    {

                    }
                }
            }

            TelegramResult(
                behaviour = result.behaviour,
                doFinish = {
                    methodForMessage(it)
                    methodForUser()
                }
            )
        }
    }

    private suspend fun saveMessage(chatId: String): (suspend (messageIds: Pair<String,Int>) -> Unit) {
        return {
            telegramInteractor.saveMessage(chatId, it)
        }
    }

    private suspend fun deleteMessage(chatId: String): (suspend (messageIds: Pair<String,Int>) -> Unit) {
        return {
            telegramInteractor.
        }
    }
}