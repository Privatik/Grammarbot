package com.io.telegram

sealed interface TelegramResult{
    data class Ordinary(
        val behaviour: TelegramBehaviour,
        val doFinish: suspend (messageIds: Pair<Int, String>) -> Unit
    ): TelegramResult

    data class Delay(
        val behaviour: Ordinary,
        val behaviours: List<suspend (Int) -> Ordinary>,
    ): TelegramResult {
        fun asOrderSendBehaviour(): TelegramBehaviour.OrderSend{
            return TelegramBehaviour.OrderSend(
                init = behaviour.behaviour,
                behaviours = behaviours.map { getOrdinary ->
                    { id -> getOrdinary(id).behaviour }
                }
            )
        }
    }
}

