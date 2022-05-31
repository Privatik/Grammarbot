package com.io.di

import com.io.cache.MessageCache
import com.io.cache.impl.MessageCacheImpl
import com.io.cache.SectionCache
import com.io.cache.TaskCache
import com.io.cache.UserCache
import com.io.cache.impl.SectionCacheImpl
import com.io.cache.impl.TaskCacheImpl
import com.io.cache.impl.UserCacheImpl
import com.io.interactor.*
import com.io.telegram.*
import com.io.telegram.TelegramBotFacade
import com.io.telegram.TelegramMessageHandlerImpl
import com.io.util.GetMessageEntityViaIntToMessageGroup
import com.io.util.GetUserEntity
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
    single<MessageCache> { MessageCacheImpl() }
    single<UserCache> { UserCacheImpl(get()) }
    single<TaskCache> { TaskCacheImpl() }
    single<SectionCache> { SectionCacheImpl() }
}

val interactorModule = module {
    factory<TelegramInteractor<GetMessageEntityViaIntToMessageGroup, GetUserEntity>> {
        TelegramInteractorImpl(get(), get())
    }
    factory<MessageInteractor<GetMessageEntityViaIntToMessageGroup>> {
        MessageInteractorImpl(get(), get(), get())
    }
    factory<UserInteractor<GetUserEntity>> {
        UserInteractorImpl(get())
    }
}