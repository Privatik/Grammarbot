package com.io.interactor

import com.io.cache.MessageCache
import com.io.cache.SectionCache
import com.io.cache.TaskCache
import com.io.cache.entity.MessageEntity
import com.io.model.MessageGroup
import com.io.util.GetBooleanViaT
import com.io.util.GetMessageEntityViaIntToMessageGroup

interface MessageInteractor<T>{

    suspend fun saveMessage(chatId: String): T
    suspend fun saveAsSectionMessage(chatId: String, sectionId: String): T
    suspend fun saveAsLearnMessage(chatId: String, taskId: Long): T

    suspend fun deleteMessages(chatId: String): T

    suspend fun getMessages(chatId: String, term: GetBooleanViaT<MessageEntity> ): List<MessageEntity>

    sealed class BehaviorForMessages {
        object Save: BehaviorForMessages()
        data class SaveAsSection(val sectionId: String): BehaviorForMessages()
        data class SaveAsTask(val taskId: Long): BehaviorForMessages()
        object Delete: BehaviorForMessages()
        object None: BehaviorForMessages()
    }
}


class MessageInteractorImpl(
    private val messageCache: MessageCache,
    private val sectionCache: SectionCache,
    private val taskCache: TaskCache,
): MessageInteractor<GetMessageEntityViaIntToMessageGroup> {

    override suspend fun saveMessage(chatId: String): GetMessageEntityViaIntToMessageGroup {
        return { id, group ->
            messageCache.saveMessageId(chatId, id, group)
        }
    }

    override suspend fun saveAsSectionMessage(chatId: String, sectionId: String): GetMessageEntityViaIntToMessageGroup {
        return { id, _ ->
            val message = messageCache.saveMessageId(chatId, id, MessageGroup.SECTION)
            sectionCache.saveMessage(chatId, id, sectionId)
            message
        }
    }

    override suspend fun saveAsLearnMessage(chatId: String, taskId: Long): GetMessageEntityViaIntToMessageGroup {
        return { id, _ ->
            val message = messageCache.saveMessageId(chatId, id, MessageGroup.LEARN)
            taskCache.saveTask(chatId, id, taskId)
            message
        }
    }

    override suspend fun deleteMessages(
        chatId: String,
    ): GetMessageEntityViaIntToMessageGroup{
        return { id, _ ->
            taskCache.deleteMessageTask(id) { it.chatId == chatId }
            sectionCache.deleteMessages(id) { it.chatId == chatId }
            messageCache.deleteMessage(chatId, id)
        }
    }

    override suspend fun getMessages(chatId: String, term: GetBooleanViaT<MessageEntity>): List<MessageEntity> {
        return messageCache.getMessages(chatId, term)
    }

}

