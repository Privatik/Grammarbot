package com.io.model

enum class MessageGroup {
    START,
    CHOICE_SECTION,
    SECTION,
    TASK,
    ANSWER_ON_TASK,
    RIGHT_ANSWER_ON_TASK,
    DESCRIBE_ERROR,
    RESULT,
    NONE
}

fun String.asMessageGroup(): MessageGroup{
    return when (this){
        MessageGroup.START.name -> MessageGroup.START
        MessageGroup.CHOICE_SECTION.name -> MessageGroup.CHOICE_SECTION
        MessageGroup.SECTION.name -> MessageGroup.SECTION
        MessageGroup.NONE.name -> MessageGroup.NONE
        MessageGroup.TASK.name -> MessageGroup.TASK
        MessageGroup.ANSWER_ON_TASK.name -> MessageGroup.ANSWER_ON_TASK
        MessageGroup.RIGHT_ANSWER_ON_TASK.name -> MessageGroup.RIGHT_ANSWER_ON_TASK
        MessageGroup.DESCRIBE_ERROR.name -> MessageGroup.DESCRIBE_ERROR
        MessageGroup.RESULT.name -> MessageGroup.RESULT
        else -> throw NoSuchMethodError()
    }
}