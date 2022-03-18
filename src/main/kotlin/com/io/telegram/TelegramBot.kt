package com.io.telegram

import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

class TelegramBot(
    private val botToken: String,
    private val botName: String,
    private val botPath: String
): TelegramWebhookBot() {
    private val facade: TelegramBotFacade by inject(TelegramBotFacade::class.java)

    override fun getBotPath(): String = botPath
    override fun getBotToken(): String = botToken
    override fun getBotUsername(): String = botName

    fun onWebhookUpdateReceived(update: com.io.model.Update): BotApiMethod<*>? {
        facade.handleMessage(update)?.also {
            executeAsync(it)
        }
        return null
    }

    override fun onWebhookUpdateReceived(update: Update): BotApiMethod<*>? = null
}