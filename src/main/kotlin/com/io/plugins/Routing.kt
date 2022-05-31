package com.io.plugins

import com.io.telegram.Update
import com.io.telegram.TelegramBot
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import org.koin.ktor.ext.inject


fun Application.configureRouting() {
    val telegramBot: TelegramBot by inject()

    routing {
        post("/") {
            val update = call.receiveOrNull<Update>() ?: kotlin.run{
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            call.respond(HttpStatusCode.OK)
            telegramBot.onWebhookUpdateReceived(update)
        }
    }
}
