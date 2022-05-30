package com.io.telegram

data class TelegramResult(
    val behaviour: TelegramBehaviour,
    val doFinish: suspend (messageIds: Pair<Long, String>) -> Unit
)
