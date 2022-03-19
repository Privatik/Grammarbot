@file:OptIn(InternalCoroutinesApi::class)

package com.io.util

import kotlinx.coroutines.*
import org.telegram.telegrambots.bots.TelegramWebhookBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer
import kotlin.coroutines.CoroutineContext

fun <T: java.io.Serializable> TelegramWebhookBot.executeAsyncMessage(botApiMethod: BotApiMethod<T>){
    executeAsync(botApiMethod)
}