package com.edem.bot.utills

fun String.isInteger() = this.toIntOrNull()?.let { true } ?: false

fun String.isKab() =
    """^[0-9,\s]+$""".toRegex().matches(this)

