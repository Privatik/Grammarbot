package com.io.util

import com.io.builder.InlineKeyBoardMarkupBuilder
import com.io.cache.entity.MessageEntity
import com.io.cache.entity.UserEntity
import com.io.interactor.TelegramInteractor
import com.io.interactor.UserInteractor
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.resourse.ChoiceLessonMessage
import com.io.resourse.Message
import com.io.resourse.StartMessage
import com.io.telegram.ReplyKeyboard
import com.io.util.extends.createMessage
import java.util.*

suspend fun TelegramInteractor<GetMessageEntityViaIntToMessageGroup, GetUserEntity>.getUserToMessageIds(
    id: String
): Pair<UserEntity, GetListRViaFuncT<MessageEntity, TypeMessage>> {
    val currentUser = processingUser(id, UserInteractor.BehaviorForUser.GetOrCreate).invoke()
    val messageIds = getMessages(id)
    return currentUser!! to messageIds
}

fun TypeMessage.getMessage(): Message{
    return when (this.message.group){
        MessageGroup.START -> StartMessage
        MessageGroup.CHOICE_SECTION -> ChoiceLessonMessage
        MessageGroup.SECTION -> (this as TypeMessage.Section).section.createMessage()
        else -> throw NoSuchMethodError()
    }
}
fun TypeMessage.getReply(): ReplyKeyboard?{

    return when (this.message.group){
        MessageGroup.CHOICE_SECTION -> InlineKeyBoardMarkupBuilder()
            .addTranslateButton()
            .build()
        MessageGroup.SECTION -> InlineKeyBoardMarkupBuilder()
            .addTranslateButton()
            .build()
        else -> null
    }
}


fun List<MessageEntity>.toMapMessageGroupWithIds(): Map<MessageGroup, List<Long>>{
    return groupByTo(EnumMap(MessageGroup::class.java), {it.group}, {it.id})
}