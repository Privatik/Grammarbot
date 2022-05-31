package com.io.plugins

import com.io.di.botModule
import com.io.di.interactorModule
import com.io.di.serviceModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin) {
        val userName = environment.config.propertyOrNull("telegramBot.userName")?.getString()
        val botToken = environment.config.propertyOrNull("telegramBot.botToken")?.getString()
        val webHookPath = environment.config.propertyOrNull("telegramBot.webHookPath")?.getString()
        val isDebug = environment.config.propertyOrNull("server.debug")?.getString().toBoolean()

        modules(
            botModule(
                botName = userName!!,
                botToken = botToken!!,
                botPath = webHookPath!!,
                isDebug = isDebug
            ),
            interactorModule,
            serviceModule
        )
    }
}