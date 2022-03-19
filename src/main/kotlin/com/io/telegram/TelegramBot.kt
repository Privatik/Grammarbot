package com.io.telegram

import com.io.util.executeAsyncMessage
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

    override fun getBotPath(): String = botPath
    override fun getBotToken(): String = botToken
    override fun getBotUsername(): String = botName

    suspend fun onWebhookUpdateReceived(update: com.io.telegram.Update) {
        facade.handleUpdate(update)?.also {
            executeAsyncMessage(it)
        }
    }

    override fun onWebhookUpdateReceived(update: Update): BotApiMethod<*>? = null
}