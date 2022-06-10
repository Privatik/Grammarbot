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
                    is TelegramResult.Order -> it.asOrderSendBehaviour()
                    is TelegramResult.Ordinary -> it.behaviour
                }
            }

            method.sendMessageByBehaviour(bodies)
                .forEachIndexed { index, body ->
                    when(body){
                        is TelegramResponseBody.Order -> {
                            val behaviour = results[index] as TelegramResult.Order
                            behaviour.doFinish(body.bodies.map { it.id to it.name })
                        }
                        is TelegramResponseBody.Ordinary -> {
                            val behaviour = results[index] as TelegramResult.Ordinary
                            behaviour.doFinish(body.id to body.name)
                        }
                    }
                }
        }
    }

    override fun onWebhookUpdateReceived(update: Update): BotApiMethod<*>? = null
}