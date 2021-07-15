package com.edem.bot.utills

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
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

        fun getInlineMarkup(buttons : List<List<String>>) : InlineKeyboardMarkup {
            val markup = InlineKeyboardMarkup()
            markup.keyboard = buttons.map { rowButtons ->
                val row = mutableListOf<InlineKeyboardButton>()
                rowButtons.forEach { rowButton ->
                    row.add(InlineKeyboardButton().apply { text = rowButton; callbackData = text })
                }
                row
            }
            return markup
        }
    }
}