package com.io.cache.entity

import com.io.model.Language
import com.io.model.UserState

data class UserEntity(
    val chatId: String,
    val currentLanguage: Language,
    val currentState: UserState
)