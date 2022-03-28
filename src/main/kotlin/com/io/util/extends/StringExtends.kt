package com.io.util.extends

import com.io.model.MessageGroup

fun String.get(): MessageGroup =
    when (this){
        MessageGroup.START.name -> MessageGroup.START
        MessageGroup.CHOICE_SECTION.name -> MessageGroup.CHOICE_SECTION
        MessageGroup.SECTION.name -> MessageGroup.SECTION
        MessageGroup.NONE.name -> MessageGroup.NONE
        else -> error("Not found MessageGroup - $this")
    }

fun Any.name(): String{
    return this::class.java.simpleName
}