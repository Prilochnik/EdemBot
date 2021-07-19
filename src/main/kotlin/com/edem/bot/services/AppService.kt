package com.edem.bot.services

import com.edem.bot.entities.AppEntity
import com.edem.bot.repos.AppRepository
import org.springframework.stereotype.Service

@Service
class AppService(
        private val appRepository: AppRepository
) {
    fun getApps() = appRepository.findAll().toList()

    fun getAppsFormat() : List<List<String>> =
            appRepository.findAll().toList()
                .filter { it.status == "live" }
                .map {
                    listOf("${it.appIde!!} ${it.appName}")
                }
}