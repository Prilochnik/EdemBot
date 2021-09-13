package com.edem.bot.utills

fun String.isInteger() = this.toIntOrNull()?.let { true } ?: false

fun String.isKab() =
    """^[0-9,\s]+$""".toRegex().matches(this)

fun String.isKabEnter() =
    """^[0-9\n]+$""".toRegex().matches(this)

fun String.isUakKabEnter() =
    """^[0-9-\n]+$""".toRegex().matches(this)

fun String.isNaming() =
        this.startsWith("https://edems.site/")

//
//fun main(){
//    val text = "645-382-5715\n" +
//            "349-976-8482\n" +
//            "424-078-1204\n" +
//            "881-665-2234\n" +
//            "162-934-2589"
//    val reg = """^[0-9-\n]+$""".toRegex().matches(text)
//    println(text)
//    println(reg)
//}
