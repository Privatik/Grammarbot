package com.io.model

import com.io.cache.entity.Entity.MessageEntity
import com.io.cache.entity.Entity.SectionRuleEntity
import com.io.cache.entity.Entity.Task
import com.io.cache.entity.ResultEntity
import com.io.cache.entity.SectionEntity
import com.io.telegram.InlineKeyboardMarkup
import com.io.telegram.ReplyKeyboard

sealed class TypeMessage(val message: MessageEntity){
    class Info(message: MessageEntity): TypeMessage(message)
    class SectionMenu(message: MessageEntity, val sections: List<SectionEntity>): TypeMessage(message)
    class Section(message: MessageEntity, val section: SectionRuleEntity): TypeMessage(message)
    class Learn(message: MessageEntity, val task: Task): TypeMessage(message)
    class Result(message: MessageEntity, val result: ResultEntity): TypeMessage(message)
}
