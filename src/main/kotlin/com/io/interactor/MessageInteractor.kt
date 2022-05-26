package com.io.interactor

import com.io.cache.MessageCache
import com.io.cache.entity.MessageEntity
import com.io.model.TypeMessage
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaT
import com.io.util.GetMessageEntityViaIntToMessageGroup

interface MessageInteractor<T>{

    suspend fun saveMessage(chatId: String): T

    suspend fun saveAsSectionMessage(chatId: String, sectionId: String): T

    suspend fun deleteMessage(chaId: String): T

    suspend fun getMessage(chaId: String, term: suspend (MessageEntity) -> Boolean ): List<TypeMessage>

    sealed class BehaviorForMessages {
        object Save: BehaviorForMessages()
        data class SaveAsSection(val sectionId: String): BehaviorForMessages()
        object Delete: BehaviorForMessages()
        object None: BehaviorForMessages()
    }
}


class MessageInteractorImpl(
    private val messageCache: MessageCache,
): MessageInteractor<GetMessageEntityViaIntToMessageGroup> {

    override suspend fun saveMessage(chatId: String, supportId: Any?): GetMessageEntityViaIntToMessageGroup {
        return { id, group ->
            if (group == MessageGroup.SECTION){

            }
            messageCache.saveMessageId(chatId, id, group)
        }
    }

    override suspend fun deleteMessage(
        chaId: String
    ): GetMessageEntityViaIntToMessageGroup {
        return { id, _ ->
            messageCache.deleteMessageId(chaId, id)
        }
    }

    override suspend fun getMessage(
        chaId: String,
        term: GetBooleanViaT<MessageEntity>
    ): List<TypeMessage> {
        return messageCache.getMessages(chaId, term)
    }

}

