package com.edem.bot.repos

import com.edem.bot.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<UserEntity, Long> {
    fun findUserEntityByUserId(userId : String) : Optional<UserEntity>
}