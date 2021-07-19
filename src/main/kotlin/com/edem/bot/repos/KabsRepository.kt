package com.edem.bot.repos

import com.edem.bot.entities.AppEntity
import com.edem.bot.entities.KabEntity
import org.springframework.data.jpa.repository.JpaRepository

interface KabsRepository : JpaRepository<KabEntity, Long> {
}