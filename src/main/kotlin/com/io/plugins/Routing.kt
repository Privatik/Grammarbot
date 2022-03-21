package com.io.plugins

import com.io.telegram.Update
import com.io.telegram.TelegramBot
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject


fun Application.configureRouting() {
    val telegramBot: TelegramBot by inject()

    routing {
        post("/") {
            val update = call.receiveOrNull<Update>() ?: kotlin.run{
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            telegramBot.onWebhookUpdateReceived(update)
            call.respond(HttpStatusCode.OK)
        }
    }
}
