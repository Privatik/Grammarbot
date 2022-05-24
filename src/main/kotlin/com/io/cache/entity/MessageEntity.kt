package com.io.cache.entity

import com.io.model.MessageGroup

data class MessageEntity(
    val id: Int,
    val chatId: String,
    val sectionId: String? = null,
    val group: MessageGroup,
    val hasTranslateButton: Boolean,
    val hasSectionButtons: Boolean
) {
    fun isHasKeyBoardButtons(): Boolean{
        return hasSectionButtons || hasTranslateButton
    }
}
