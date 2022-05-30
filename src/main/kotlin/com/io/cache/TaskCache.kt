package com.io.cache

import com.io.cache.entity.MessageToTask
import com.io.cache.entity.Task
import com.io.model.LessonState
import com.io.util.GetBooleanViaT


interface TaskCache {

    suspend fun saveTask(chatId: String, messageId: Long, taskId: Long, state: LessonState)
    suspend fun deleteMessageTask(messageId: Long, term: GetBooleanViaT<MessageToTask>)

    suspend fun getRandomTaskFromSection(sectionId: String, state: LessonState): Task?
    suspend fun getCurrentTask(messageId: Long): Task
}