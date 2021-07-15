package com.edem.bot.services

import org.springframework.stereotype.Service

@Service
class AppService {
    fun getApps(): List<String> {
        return listOf<String>("App1", "App2")
    }
}