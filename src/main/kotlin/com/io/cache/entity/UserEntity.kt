package com.io.cache.entity

import com.io.model.Language

data class UserEntity(
    val chatId: String,
    val currentLanguage: Language,
    val currentState: UserState
)

enum class UserState{
    LEARN,
    RELAX
}