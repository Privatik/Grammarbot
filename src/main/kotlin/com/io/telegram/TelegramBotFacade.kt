package com.io.telegram

import com.io.model.Update
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

class TelegramBotFacade {

    fun handleMessage(update: Update): SendMessage?{
        if (update.hasMessage() && update.message!!.hasText()){
            val id = update.message.chat.id

            return SendMessage(id.toString(),"Hi ${update.message.text}")
        }
        return null
    }
}