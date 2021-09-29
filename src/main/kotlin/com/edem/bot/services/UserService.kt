package com.edem.bot.services

import com.edem.bot.entities.UserEntity
import com.edem.bot.repos.AppRepository
import com.edem.bot.repos.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val appRepository: AppRepository
) {

    fun findUser(id : Long) : UserEntity{
        val user = userRepository.findUserEntityByUserId(id.toString()).orElse(UserEntity())
        return user
    }

    fun addAppToUser(appIde : String, userId : Long){
        val user = userRepository.findUserEntityByUserId(userId.toString()).orElseThrow { Exception("No user found") }
        if(user.apps == "" || user.apps == " " || user.apps == null) user.apps = appIde
        else user.apps = "${user.apps},$appIde"
        userRepository.save(user)
    }

    fun addUser(id: Long) {
        val user = UserEntity(
            userGroup = "wait",
            userId = id.toString(),
            apps = ""
        )
        userRepository.save(user)
    }

    fun findAppsForGroup(id : Long): List<String> {
        val user = userRepository.findUserEntityByUserId(id.toString()).orElseThrow { Exception("No user found") }
        if(user.userGroup == "admin"){
            user.apps = mapAdminAppsToSave()
            userRepository.save(user)
        }
        if(user.userGroup == "buyer"){
            user.apps = mapBuyerAppsToSave()
            userRepository.save(user)
        }
        var apps = user.apps?.split(",")
        if(apps == null) apps = listOf("")
        return apps
    }

    fun mapAdminAppsToSave() = appRepository.findAll().toList()
        .filter { it.status == "live" || it.status == "metka" }
        .map { it.appIde }.joinToString(",")

    fun mapBuyerAppsToSave() = appRepository.findAll().toList()
        .filter { it.status == "live" }
        .map { it.appIde }.joinToString(",")
}
