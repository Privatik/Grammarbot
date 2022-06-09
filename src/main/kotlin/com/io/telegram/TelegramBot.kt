package com.io.telegram

import org.koin.java.KoinJavaComponent.inject
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

class TelegramBot(
    private val botToken: String,
    private val botName: String,
    private val botPath: String
): TelegramWebhookBot() {
    private val facade: TelegramBotFacade by inject(TelegramBotFacade::class.java)
    private val method: TelegramMethod by inject(TelegramMethod::class.java)

    override fun getBotPath(): String = botPath
    override fun getBotToken(): String = botToken
    override fun getBotUsername(): String = botName

    suspend fun onWebhookUpdateReceived(update: com.io.telegram.Update) {
        facade.handleUpdate(update)?.also { results ->
            val bodies = results.map {
                when (it){
                    is TelegramResult.Delay -> it.asOrderSendBehaviour()
                    is TelegramResult.Ordinary -> it.behaviour
                }
            }

            method.sendMessageByBehaviour(bodies)
                .forEachIndexed { index, pair ->
                    when (val currentResult = results[index]){
                        is TelegramResult.Delay -> {
                            currentResult.behaviour.doFinish(pair)
                            var nextPair = pair
                            currentResult.behaviours.forEach { getOrdinary ->
                                val ordinary = getOrdinary(nextPair.first)
                                ordinary.doFinish(nextPair)
                                nextPair =
                            }
                        }
                        is TelegramResult.Ordinary -> {
                            currentResult.doFinish(pair)
                        }
                    }
                }
        }
    }

    override fun onWebhookUpdateReceived(update: Update): BotApiMethod<*>? = null
}