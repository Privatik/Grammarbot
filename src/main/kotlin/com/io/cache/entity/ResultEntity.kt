package com.io.cache.entity

data class ResultEntity(
    val chatId: String,
    val topic: String,
    val positive: Int,
    val negative: Int,
    val lastPositive: Int,
    val lastNegative: Int
)
