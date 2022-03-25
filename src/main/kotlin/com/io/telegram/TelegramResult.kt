package com.io.telegram

data class TelegramResult(
    val behaviours: List<TelegramBehaviour>,
    val doFinish: suspend (messageIds: List<Int>) -> Unit
)
