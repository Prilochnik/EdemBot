package com.edem.bot.repos

import com.edem.bot.entities.AppEntity
import org.springframework.data.jpa.repository.JpaRepository

interface AppRepository : JpaRepository<AppEntity, Long> {
    fun findAppEntityByAppIde(appIde : String) : AppEntity
}