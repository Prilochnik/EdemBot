package com.edem.bot.services

import com.edem.bot.entities.AppEntity
import com.edem.bot.repos.AppRepository
import com.edem.bot.repos.UserRepository
import org.springframework.stereotype.Service

@Service
class AppService(
        private val appRepository: AppRepository,
) {
    fun getApps() = appRepository.findAll().toList()

    fun getAppsFormat() : List<List<String>> =
            appRepository.findAll().toList()
                .filter { it.status == "live" }
                .map {
                    listOf("${it.appIde!!} ${it.appName}")
                }

    fun createLink(appName : String) =
            "https://play.google.com/store/apps/details?id=${appRepository.findAppEntityByAppIde(appName.split(" ")[0]).appPackage}"

    fun formatAppsForUser(apps : List<String>): List<List<String>> {
        if(apps.size == 1 && (apps[0] == "" || apps[0] == " ")) return listOf(listOf())
        return apps.map {
                appRepository.findAppEntityByAppIde(it)
            }
            .map {
                listOf("${it.appIde!!} ${it.appName}")
            }
    }
}