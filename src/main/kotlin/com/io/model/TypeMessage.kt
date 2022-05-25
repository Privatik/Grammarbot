package com.io.model

import com.io.cache.entity.MessageEntity
import com.io.cache.entity.SectionRuleEntity

sealed class TypeMessage(val message: MessageEntity){
    class Simple(message: MessageEntity): TypeMessage(message)
    class Section(message: MessageEntity, val section: SectionRuleEntity): TypeMessage(message)
}
