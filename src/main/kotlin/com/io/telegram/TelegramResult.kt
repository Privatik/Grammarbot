package com.io.telegram

sealed interface TelegramResult{
    data class Ordinary(
        val behaviour: TelegramBehaviour,
        val doFinish: suspend (messageIds: Pair<Int, String>) -> Unit
    ): TelegramResult

    data class Order(
        val behaviour: Ordinary,
        val behaviours: List<Pair<suspend (Int) -> Ordinary,String>>,
        val doFinish: suspend (messageIds: List<Pair<Int, String>>) -> Unit
    ): TelegramResult {
        fun asOrderSendBehaviour(): TelegramBehaviour.OrderSend{
            return TelegramBehaviour.OrderSend(
                init = behaviour.behaviour,
                behaviours = behaviours.map {
                    it.first.getBehaviour() to it.second
                }
            )
        }
    }
}

private fun (suspend (Int) -> TelegramResult.Ordinary).getBehaviour(): suspend (Int) -> TelegramBehaviour {
    return { this(it).behaviour }
}

