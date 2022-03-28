package com.io.telegram

sealed class TelegramBehaviour(val name: String){
    data class Send(
        val cName: String,
        val request: TelegramRequest,
        val delay: Long = 0
    ): TelegramBehaviour(cName)

    data class UpdateBehaviour(
        val cName: String,
        val body: TelegramRequest,
        val editMessageTextRequest: TelegramRequest.EditMessageTextRequest,
        val delay: Long = 0,
        val nextDelay: Long = 0
    ): TelegramBehaviour(cName)
}