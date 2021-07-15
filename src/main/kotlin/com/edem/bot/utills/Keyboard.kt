package com.edem.bot.utills

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow

class Keyboard {
    companion object{
        fun getReplyMarkup(allButtons: List<List<String>>): ReplyKeyboardMarkup {
            val markup = ReplyKeyboardMarkup()
            markup.keyboard = allButtons.map { rowButtons ->
                val row = KeyboardRow()
                rowButtons.forEach { rowButton -> row.add(rowButton) }
                row
            }
            return markup
        }
    }
}