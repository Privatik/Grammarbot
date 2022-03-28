package com.io.di

import com.io.interactor.TelegramInteractor
import com.io.interactor.TelegramInteractorImpl
import com.io.cache.MessageCache
import com.io.cache.impl.MessageCacheImpl
import com.io.cache.SectionCache
import com.io.cache.TaskCache
import com.io.cache.UserCache
import com.io.cache.impl.SectionCacheImpl
import com.io.cache.impl.TaskCacheImpl
import com.io.cache.impl.UserCacheImpl
import com.io.telegram.*
import com.io.telegram.TelegramBotFacade
import com.io.telegram.TelegramMessageHandlerImpl
import org.koin.dsl.module

fun botModule(
    botToken: String,
    botName: String,
    botPath: String,
    isDebug: Boolean
) = module {
    single {
        TelegramBot(
            botToken = botToken,
            botName = botName,
            botPath = botPath
        )
    }

    factory { TelegramBotFacade(get(), get()) }
    factory { TelegramMethod(botToken, isDebug) }
    factory<TelegramMessageHandler> { TelegramMessageHandlerImpl() }
}

val serviceModule = module {
    factory<MessageCache> { MessageCacheImpl() }
    factory<UserCache> { UserCacheImpl(get()) }
    factory<TaskCache> { TaskCacheImpl() }
    factory<SectionCache> { SectionCacheImpl() }
}

val interactorModule = module {
    factory<TelegramInteractor> { TelegramInteractorImpl(get(), get()) }
}