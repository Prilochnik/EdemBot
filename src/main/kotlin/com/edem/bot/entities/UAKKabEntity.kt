package com.edem.bot.entities

import javax.persistence.*

@Entity
@Table(name = "uakkabs")
data class UAKKabEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id : Long? = null,
    var kab : String? = null,
    var chat_id : String? = null,
    var status : String? = null,
    var app_ide : String? = null,
)
