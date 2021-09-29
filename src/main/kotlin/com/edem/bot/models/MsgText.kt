package com.edem.bot.models

class MsgText {
    companion object{
        const val START = "Hi"
        const val CHOOSE_APP = "Ну приветики, на какую прилу желаешь залится?"
        const val ERROR = "ERorr?"
        const val ADD_ADS_ID = "Для добавления РК, введите ID через запятую, без пробелов или в столбик без пробелов и запятых"
        const val UAK_ADS = "Для добавления РК, введите ID в столбик без пробелов и запятых"
        const val ORGANIC = "Organic"
        const val CREATE_NAMING = "Введите ссылку с доменом edem.site"
        const val REMOVE_AD_ID = "Za chto?"
        const val KABS_IN_PROCESS = "Кабинеты в процессе добавления"

        fun authToken(token : String) = "Your user id is $token"
        fun successful(option : String) = "$option в процессе"
        fun appChosen(appId : String, link : String) = "Допустим ты выбрал ${appId}, \n$link?"
    }
}