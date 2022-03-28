package com.io.plugins

import com.io.telegram.TelegramHttpClient
import com.io.telegram.TelegramMethod
import com.io.telegram.TelegramRequest
import com.io.telegram.WebhookInfo
import io.ktor.application.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject

fun Application.configureTelegramWebhook() {
    val method: TelegramMethod by inject()
    val webHookPath = environment.config.propertyOrNull("telegramBot.webHookPath")?.getString()!!

    val scope = CoroutineScope(Dispatchers.IO)

    scope.launch {

        val response = method.get(
            request = TelegramRequest.GetWebhookRequest,
            serializer = WebhookInfo.serializer()
        )

        if (response.url != webHookPath){
            method.get<TelegramHttpClient.EmptyResponse>(
                request = TelegramRequest.SetWebhookRequest,
                params = mapOf<String, Any>("url" to webHookPath)
            )
        }
        scope.cancel()
    }

}