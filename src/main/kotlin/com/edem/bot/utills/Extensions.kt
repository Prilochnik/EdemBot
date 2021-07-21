package com.edem.bot.utills

fun String.isInteger() = this.toIntOrNull()?.let { true } ?: false

fun String.isKab() {
    val regex = """^[0-9,\s]+$""".toRegex().matches(this)
}
