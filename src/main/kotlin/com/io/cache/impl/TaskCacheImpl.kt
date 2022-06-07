package com.io.cache.impl

import com.io.cache.TaskCache
import com.io.cache.entity.*
import com.io.model.LessonState
import com.io.util.GetBooleanViaT
import com.io.cache.entity.Entity.Task.PutTaskEntity
import com.io.cache.entity.Entity.Task.WriteTaskEntity
import com.io.cache.entity.Entity.Task
import com.io.util.extends.getRandomItemOrNull
import kotlin.random.Random

class TaskCacheImpl: TaskCache {
    private val messagesWithTask = mutableListOf<MessageToTask>()

    val tasksPut = listOf(
        PutTaskEntity(
            id = 1L,
            field = "My name _ Ivan",
            variants = listOf("is","are","am"),
            rightAnswer = "is",
            sectionId = "present_simple"
        ),
        PutTaskEntity(
            id = 2L,
            field = "He _ a young years ago",
            variants = listOf("were","was"),
            rightAnswer = "was",
            sectionId = "past_simple"
        )
    )

    val tasksWrite = listOf(
        WriteTaskEntity(
            id = 3L,
            field = "Мое имя Иван",
            rightAnswer = "My name is Ivan",
            sectionId = "present_simple"
        ),
        WriteTaskEntity(
            id = 4L,
            field = "Он был молод год назад",
            rightAnswer = "He was a young years ago",
            sectionId = "past_simple"
        )
    )

    val tasks = tasksPut + tasksWrite


    override suspend fun saveTask(chatId: String, messageId: Int, taskId: Long, state: LessonState) {
        messagesWithTask.add(
            MessageToTask(chatId, messageId, taskId, state)
        )
    }

    override suspend fun deleteMessageTask(messageId: Int, term: GetBooleanViaT<MessageToTask>) {
        messagesWithTask.removeIf { term(it) && messageId == it.messageId }
    }

    override suspend fun getTask(chatId: String, term: GetBooleanViaT<Task>): List<Task> {
        return tasks.filter(term)
    }

    override suspend fun getCurrentTask(messageId: Int): Task {
        val message = messagesWithTask.find { it.messageId == messageId }!!
        return when (message.lessonState){
            LessonState.PUT -> tasksPut.find { message.taskId == it.id }!!
            LessonState.WRITE -> tasksWrite.find { message.taskId == it.id }!!
        }
    }

}