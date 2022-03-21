package com.io.plugins

import com.io.di.botModule
import com.io.di.cacheTestModule
import io.ktor.application.*
import org.koin.ktor.ext.Koin

fun Application.configureKoin() {
    install(Koin) {
        val userName = environment.config.propertyOrNull("telegramBot.userName")?.getString()
        val botToken = environment.config.propertyOrNull("telegramBot.botToken")?.getString()
        val webHookPath = environment.config.propertyOrNull("telegramBot.webHookPath")?.getString()

        modules(
            botModule(
                botName = userName!!,
                botToken = botToken!!,
                botPath = webHookPath!!
            ),
            cacheTestModule
        )
    }
}