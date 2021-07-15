package com.edem.bot.models

data class ResponseModel(
        val chatId : Long,
        val msg : String,
        val buttons : List<List<String>>
)