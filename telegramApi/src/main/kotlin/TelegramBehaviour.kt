package com.io.telegram

sealed class TelegramBehaviour(val name: String){
    data class Send(
        val cName: String,
        val request: TelegramRequest,
        val delay: Long = 0
    ): TelegramBehaviour(cName)

    data class Delete(
        val cName: String,
        val request: TelegramRequest,
        val deleteMessageId: Int,
        val delay: Long = 0
    ) : TelegramBehaviour(cName)
}