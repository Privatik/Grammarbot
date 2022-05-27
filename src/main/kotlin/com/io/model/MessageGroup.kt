package com.io.model

enum class MessageGroup {
    START,
    CHOICE_SECTION,
    SECTION,
    LEARN,
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
        else -> error("Don't find group")
    }
}