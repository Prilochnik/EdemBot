package com.edem.bot.models

import com.edem.bot.entities.AppEntity

class Button {

    companion object{
        const val addAdsButtonText = "Добавить Рк"
        const val namingButtonText : String = "Сделать Нейминг"
        const val organicButtonText : String = "Органика"
        const val cancel : String = "Бля, не то"
        const val add : String = "Добавляй заебал"
        const val BACK : String = "<- Nazad"

        fun link(app : String, apps : List<AppEntity>) : String {
            apps.forEach {
                if("${it.appIde!!} ${it.appName}" == app) return "https://play.google.com/store/apps/details?id=${it.appPackage}"
            }
            return ""
        }
    }


}