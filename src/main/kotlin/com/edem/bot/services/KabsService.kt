package com.edem.bot.services

import com.edem.bot.entities.KabEntity
import com.edem.bot.repos.KabsRepository
import org.springframework.stereotype.Service

@Service
class KabsService(
        private val kabsRepository: KabsRepository
) {

    fun addKab(kab : String, chatId : Long, appId : String){
        val ad = KabEntity(
                kab = kab,
                chat_id = chatId.toString(),
                app_ide = appId,
                status = "waiting"
        )
        kabsRepository.save(ad)
    }

}