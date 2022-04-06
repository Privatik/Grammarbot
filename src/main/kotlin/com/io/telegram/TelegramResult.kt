package com.io.telegram

data class TelegramResult(
    val behaviour: TelegramBehaviour,
    val doFinish: suspend (messageIds: Pair<Int, String>) -> Unit
)
