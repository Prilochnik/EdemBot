package com.edem.bot.utills

fun String.isInteger() = this.toIntOrNull()?.let { true } ?: false

fun String.isKab() {
    val regex = """([0-9]*,[0-9]*)*"""
}

fun main(){
    val regex = """([0-9])+""".toRegex()
    println(regex.matches("234345, 234235,"))
    //println(regex.containsMatchIn("23434g5 , 234235"))
}