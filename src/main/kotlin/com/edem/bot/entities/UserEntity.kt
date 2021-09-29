package com.edem.bot.entities

import javax.persistence.*

@Entity
@Table(name = "bot_users")
data class UserEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Long? = null,
    var userGroup : String? = null,
    var userId : String? = null,
    var apps : String? = null
)