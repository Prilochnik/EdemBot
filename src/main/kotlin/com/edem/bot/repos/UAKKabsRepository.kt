package com.edem.bot.repos

import com.edem.bot.entities.KabEntity
import com.edem.bot.entities.UAKKabEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UAKKabsRepository : JpaRepository<UAKKabEntity, Long> {
}