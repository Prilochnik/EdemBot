package com.edem.bot.models

class MsgText {
    companion object{
        const val START = "Hi"
        const val CHOOSE_APP = "Ну приветики, на какую прилу желаешь залится?"
        const val ERROR = "ERorr?"
        const val ADD_ADS_ID = "Ну добавляй, надеюсь они рабочие"
        const val ORGANIC = "Organic"
        const val CREATE_NAMING = "Naming"
        const val REMOVE_AD_ID = "Za chto?"
        const val KABS_IN_PROCESS = "Кабинеты в процессе добавления"

        fun successful(option : String) = "$option Выполнено успешно"
        fun appChosen(appId : String) = "Допустим ты выбрал ${appId}, дальше то что?"
    }
}