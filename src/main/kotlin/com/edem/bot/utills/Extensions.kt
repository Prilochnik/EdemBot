package com.edem.bot.utills

fun String.isInteger() = this.toIntOrNull()?.let { true } ?: false