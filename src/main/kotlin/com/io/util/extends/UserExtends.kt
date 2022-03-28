package com.io.util.extends

import com.io.cache.entity.UserEntity
import com.io.model.Language

fun UserEntity.anotherLanguage(): Language{
    return if (currentLanguage == Language.EN){
        Language.RU
    } else {
        Language.EN
    }
}