package com.io.di

import com.io.cache.SectionCache
import com.io.cache.TaskCache
import com.io.cache.UserCache
import com.io.cache.impl.SectionCacheImpl
import com.io.cache.impl.TaskCacheImpl
import com.io.cache.impl.UserCacheImpl
import com.io.telegram.TelegramBot
import com.io.telegram.TelegramBotFacade
import com.io.telegram.TelegramMethod
import org.koin.core.qualifier.named
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
    factory { TelegramMethod(botToken) }
}

val cacheTestModule = module {
    factory<UserCache> { UserCacheImpl() }
    factory<TaskCache> { TaskCacheImpl() }
    factory<SectionCache> { SectionCacheImpl() }
}