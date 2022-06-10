package com.io.model

import com.io.interactor.MessageInteractor
import com.io.interactor.UserInteractor
import com.io.telegram.TelegramBehaviour
import com.io.telegram.TelegramMessageHandler

data class MultiBehaviours(
    val behaviour: (Int) -> TelegramBehaviour,
    val name: String,
    val finishBehaviorUser: UserInteractor.BehaviorForUser,
    val finishBehaviorMessage: MessageInteractor.BehaviorForMessages
)
