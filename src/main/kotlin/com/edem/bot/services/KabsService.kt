package com.edem.bot.services

import com.edem.bot.entities.KabEntity
import com.edem.bot.entities.UAKKabEntity
import com.edem.bot.repos.KabsRepository
import com.edem.bot.repos.UAKKabsRepository
import org.springframework.stereotype.Service

@Service
class KabsService(
        private val kabsRepository: KabsRepository,
        private val uakKabsRepository: UAKKabsRepository
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

    fun addKabs(kabs : String, chatId: Long, appId: String){
        val kabEntities : List<KabEntity> = kabs.split(",", "\n").map {
            KabEntity(
                    kab = it,
                    chat_id = chatId.toString(),
                    app_ide = appId,
                    status = "wait"
            )
        }
        kabsRepository.saveAll(kabEntities)
    }

    fun addUAKKabs(kabs : String, chatId: Long, appId: String){
        val kabEntities : List<UAKKabEntity> = kabs.split(",", "\n").map {
            UAKKabEntity(
                kab = it,
                chat_id = chatId.toString(),
                app_ide = appId,
                status = "wait"
            )
        }
        uakKabsRepository.saveAll(kabEntities)
    }

}