package com.io.di

import com.io.interactor.TelegramInteractor
import com.io.interactor.TelegramInteractorImpl
import com.io.repository.MessageRepository
import com.io.repository.impl.MessageRepositoryImpl
import com.io.service.SectionCache
import com.io.service.TaskCache
import com.io.service.UserService
import com.io.service.impl.SectionCacheImpl
import com.io.service.impl.TaskCacheImpl
import com.io.service.impl.UserServiceImpl
import com.io.telegram.TelegramBot
import com.io.telegram.TelegramBotFacade
import com.io.telegram.TelegramMethod
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

    factory { TelegramBotFacade(get(), get(), get()) }
    factory { TelegramMethod(botToken, isDebug) }
}

val serviceModule = module {
    factory<UserService> { UserServiceImpl(get()) }
    factory<TaskCache> { TaskCacheImpl() }
    factory<SectionCache> { SectionCacheImpl() }
}

val interactorModule = module {
    factory<TelegramInteractor> { TelegramInteractorImpl(get()) }
}

val repositoryTestModule = module {
    factory<MessageRepository> { MessageRepositoryImpl() }
}