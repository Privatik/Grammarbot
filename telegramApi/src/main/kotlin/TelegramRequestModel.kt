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