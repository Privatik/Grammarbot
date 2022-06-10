package com.io.telegram

import kotlinx.serialization.Serializable

@Serializable
data class TelegramResponse<T>(
    val ok: Boolean,
    val result: T
)

@Serializable
data class MessageIdResponse(
    val message_id: Int
)

object EmptyResponse

sealed interface TelegramResponseBody {
    data class Ordinary(
        val id: Int,
        val name: String
    ): TelegramResponseBody

    data class Order(
        val bodies: List<Ordinary>
    ): TelegramResponseBody
}