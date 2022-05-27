package com.io.cache.entity

interface Task{
    val id: Long
    val sectionId: String
    val field: String
    val rightAnswer: String
}

data class WriteTaskEntity(
    override val id: Long,
    override val field: String,
    override val rightAnswer: String,
    override val sectionId: String
): Task

data class PutTaskEntity(
    override val id: Long,
    override val field: String,
    val variants: List<String>,
    override val rightAnswer: String,
    override val sectionId: String
): Task

data class CurrentTask(
    val chatId: String,
    val taskId: Long
)
