package com.io.di

import com.io.telegram.TelegramBot
import com.io.telegram.TelegramBotFacade
import org.koin.dsl.module

fun botModule(
    botToken: String,
    botName: String,
    botPath: String
) = module {
    single {
        TelegramBot(
            botToken = botToken,
            botName = botName,
            botPath = botPath
        )
    }

    factory { TelegramBotFacade() }
}