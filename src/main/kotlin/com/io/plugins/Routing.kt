package com.io.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import org.telegram.telegrambots.meta.api.objects.Update

fun Application.configureRouting() {

    routing {
        post("/") {
            val update = call.receiveOrNull<Update>()
        }
    }
}
