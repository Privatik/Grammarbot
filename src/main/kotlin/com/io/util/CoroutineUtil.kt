package com.io.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.future.await
import kotlinx.coroutines.future.future
import kotlinx.coroutines.withContext
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod

suspend fun <T: java.io.Serializable> TelegramWebhookBot.executeAsyncMessage(
    botApiMethod: BotApiMethod<T>
) = withContext(Dispatchers.IO) {
    future {
//        execute()
        executeAsync(botApiMethod)
    }
}