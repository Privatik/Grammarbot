package com.io.telegram

typealias CreateTelegramBehaviour = suspend (messageId: Int) -> TelegramBehaviour
