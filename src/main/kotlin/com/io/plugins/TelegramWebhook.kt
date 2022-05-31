package com.io.plugins

import com.io.telegram.*
import io.ktor.server.application.*
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
            method.get<EmptyResponse>(
                request = TelegramRequest.SetWebhookRequest,
                params = mapOf<String, Any>("url" to webHookPath)
            )
        }
        scope.cancel()
    }

}