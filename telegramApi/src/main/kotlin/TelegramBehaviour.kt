package com.io.telegram

sealed class TelegramBehaviour{
    data class Send(
        val request: TelegramRequest,
        val delay: Long = 0
    ): TelegramBehaviour()

    data class UpdateBehaviour(
        val body: TelegramRequest,
        val editMessageTextRequest: TelegramRequest.EditMessageTextRequest,
        val delay: Long = 0,
        val nextDelay: Long = 0
    ): TelegramBehaviour()
}