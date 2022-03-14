package com.io.telegram

import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Update

class TelegramBot: TelegramWebhookBot() {


    override fun getBotToken(): String {
        TODO("Not yet implemented")
    }

    override fun getBotUsername(): String {
        TODO("Not yet implemented")
    }

    override fun onWebhookUpdateReceived(update: Update?): BotApiMethod<*> {
        TODO("Not yet implemented")
    }

    override fun getBotPath(): String {
        TODO("Not yet implemented")
    }
}