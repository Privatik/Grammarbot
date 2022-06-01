package com.io.interactor

import com.io.cache.MessageCache
import com.io.cache.SectionCache
import com.io.cache.TaskCache
import com.io.cache.entity.Entity
import com.io.model.LessonState
import com.io.model.MessageGroup
import com.io.model.TypeMessage
import com.io.resourse.emptyMessageEntity
import com.io.util.GetBooleanViaT
import com.io.util.GetMessageEntityViaIntToMessageGroup

interface MessageInteractor<T>{

    suspend fun saveMessage(chatId: String): T
    suspend fun saveAsSectionMessage(chatId: String, sectionId: String): T
    suspend fun saveAsLearnMessage(chatId: String, taskId: Long, state: LessonState): T

    suspend fun deleteMessages(chatId: String): T

    suspend fun getMessages(
        chatId: String,
        searchRule: Pair<MessageGroup, GetBooleanViaT<Entity>>
    ): List<TypeMessage>

    sealed class BehaviorForMessages {
        object Save: BehaviorForMessages()
        data class SaveAsSection(val sectionId: String): BehaviorForMessages()
        data class SaveAsTask(val taskId: Long, val learnState: LessonState): BehaviorForMessages()
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

    override suspend fun saveAsLearnMessage(chatId: String, taskId: Long, state: LessonState): GetMessageEntityViaIntToMessageGroup {
        return { id, _ ->
            val message = messageCache.saveMessageId(chatId, id, MessageGroup.LEARN)
            taskCache.saveTask(chatId, id, taskId, state)
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

    override suspend fun getMessages(
        chatId: String,
        searchRule: Pair<MessageGroup, GetBooleanViaT<Entity>>
    ): List<TypeMessage> {
        return when(searchRule.first){
            MessageGroup.NONE,
            MessageGroup.START,
            MessageGroup.CHOICE_SECTION -> {
                messageCache.getMessages(chatId, searchRule.second)
                    .ifEmpty { listOf(emptyMessageEntity.copy(group = MessageGroup.CHOICE_SECTION)) }
                    .map { it.asTypeMessage() }
            }
            MessageGroup.SECTION -> {
                sectionCache.getRules(searchRule.second).map { it.asTypeMessage() }
            }
            MessageGroup.LEARN,
            MessageGroup.DESCRIBE_ERROR ,
            MessageGroup.RESULT -> throw NoSuchMethodError()
        }
    }

    private suspend fun Entity.MessageEntity.asTypeMessage(): TypeMessage{
        return when (group){
            MessageGroup.NONE,
            MessageGroup.RESULT,
            MessageGroup.START,
            MessageGroup.DESCRIBE_ERROR -> TypeMessage.Info(this)
            MessageGroup.CHOICE_SECTION -> TypeMessage.SectionMenu(this, sectionCache.getAllSectionInfo())
            MessageGroup.SECTION -> TypeMessage.Section(this, sectionCache.getCurrentRules(id))
            MessageGroup.LEARN -> TypeMessage.Learn(this, taskCache.getCurrentTask(id))
        }
    }

    private suspend fun Entity.SectionRuleEntity.asTypeMessage(): TypeMessage{
        return TypeMessage.Section(
            emptyMessageEntity,
            this
        )
    }

}

