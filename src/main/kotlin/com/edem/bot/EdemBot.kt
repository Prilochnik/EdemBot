package com.edem.bot

import com.edem.bot.models.MsgStates
import com.edem.bot.models.ResponseModel
import com.edem.bot.services.AppService
import com.edem.bot.utills.Button
import com.edem.bot.utills.Keyboard
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class EdemBot(
        private val appService : AppService
) : TelegramLongPollingBot() {

    @Value("\${bot.name}")
    private val botName : String = ""

    @Value("\${bot.token}")
    private val token : String = ""

    override fun onUpdateReceived(update: Update) {
        if(update.hasMessage()){
            val message = update.message
            val apps = appService.getApps()
            val chatId = message.chatId
            val responseState = if (message.hasText()) {
                val msgText = message.text
                when {
                    msgText == "/start" -> MsgStates.StartState
                    else -> MsgStates.Error
                }
            } else {
                MsgStates.Error
            }
            sendInlineMsg(chatId, responseState)
        }
        else if(update.hasCallbackQuery()) {
            val msgText = update.callbackQuery.data
            val chatId = update.callbackQuery.message.chatId
            val apps = appService.getApps()
            val responseState = when {
                apps.contains(msgText) -> MsgStates.AppChosenState("1")
                msgText == Button.addAdsButtonText -> MsgStates.AddAdsIdState("1")
                msgText == Button.namingButtonText -> MsgStates.CreateNamingState("1")
                msgText == Button.organicButtonText -> MsgStates.AddOrganicState("1")
                else -> MsgStates.Error
            }
            println(responseState)
            sendInlineMsg(chatId, responseState) //todo needs to update msg
        }
    }

    fun sendInlineMsg(chatId: Long, state : MsgStates) {
        val responseModel = when(state) {
            is MsgStates.StartState -> {
                //responseText = "Ну приветики, на какую прилу желаешь залится?"
                ResponseModel(
                        chatId = chatId,
                        msg = "Ну приветики, на какую прилу желаешь залится?",
                        buttons = listOf(appService.getApps())
                )
            }
            is MsgStates.AppChosenState -> {
                ResponseModel(
                        chatId = chatId,
                        msg = "Допустим ты выбрал ${state.appId}, дальше то что?",
                        buttons = listOf(listOf(Button.addAdsButtonText, Button.namingButtonText, Button.organicButtonText))
                )
            }
            else -> {
                ResponseModel(
                        chatId = chatId,
                        msg = "ERorr?",
                        buttons = listOf(listOf("Error", "Eh ladno"))
                )
            }
        }
        val responseMessage = SendMessage(chatId, responseModel.msg)
        responseMessage.setParseMode("Markdown")
        responseMessage.replyMarkup = Keyboard.getInlineMarkup(responseModel.buttons)
        execute(responseMessage)

    }
//    override fun onUpdateReceived(update: Update) {
//        if(update.hasMessage()){
//            val apps = appService.getApps()
//            val message = update.message
//            val chatId = message.chatId
//            val responseState = if (message.hasText()) {
//                val msgText = message.text
//                when {
//                    msgText == "/start" -> MsgStates.StartState
//                    apps.contains(msgText) -> MsgStates.AppChosenState("1")
//                    msgText == Button.addAdsButtonText -> MsgStates.AddAdsIdState("1")
//                    msgText == Button.namingButtonText -> MsgStates.CreateNamingState("1")
//                    msgText == Button.organicButtonText -> MsgStates.AddOrganicState("1")
//                    else -> MsgStates.Error
//                }
//            } else {
//                MsgStates.Error
//            }
//            sendNotification(chatId, responseState)
//        }
//    }

    private fun sendNotification(chatId: Long, state : MsgStates){
        val responseModel = when(state){
            is MsgStates.StartState -> {
                //responseText = "Ну приветики, на какую прилу желаешь залится?"
                ResponseModel(
                        chatId = chatId,
                        msg = "Ну приветики, на какую прилу желаешь залится?",
                        buttons = listOf(appService.getApps())
                )
            }
            is MsgStates.AppChosenState -> {
                ResponseModel(
                        chatId = chatId,
                        msg = "Допустим ты выбрал ${state.appId}, дальше то что?",
                        buttons = listOf(listOf(Button.addAdsButtonText, Button.namingButtonText, Button.organicButtonText))
                )
            }
            is MsgStates.AddAdsIdState -> {
                ResponseModel(
                        chatId = chatId,
                        msg = "Ну добавляй, надеюсь они рабочие",
                        buttons = listOf(listOf("Добавить", "Ладно, я передумал"))
                )
            }
            is MsgStates.AddOrganicState -> {
                ResponseModel(
                        chatId = chatId,
                        msg = "Органика",
                        buttons = listOf(listOf("Добавить", "Ладно, я передумал"))
                )
            }
            is MsgStates.CreateNamingState -> {
                ResponseModel(
                        chatId = chatId,
                        msg = "Naming",
                        buttons = listOf(listOf("Добавить", "Ладно, я передумал"))
                )
            }
            is MsgStates.RemoveAdsIdState -> {
                ResponseModel(
                        chatId = chatId,
                        msg = "Что, не пошло?",
                        buttons = listOf(listOf("Добавить", "Ладно, я передумал"))
                )
            }
            is MsgStates.Error -> {
                ResponseModel(
                        chatId = chatId,
                        msg = "Произошла Ошибка",
                        buttons = listOf(listOf("Ну да, ну да, пошел я нахер"))
                )
            }

        }
        val responseMessage = SendMessage(chatId, responseModel.msg)
        responseMessage.setParseMode("Markdown")
        responseMessage.replyMarkup = Keyboard.getReplyMarkup(responseModel.buttons)
        execute(responseMessage)
    }

//    private fun sendNotification(chatId: Long, responseText: String) {
//        val responseMessage = SendMessage(chatId, responseText)
//        responseMessage.setParseMode("Markdown")
//        responseMessage.replyMarkup = Keyboard.getReplyMarkup(
//
//        )
//        execute(responseMessage)
//    }

    override fun getBotToken(): String = token

    override fun getBotUsername(): String = botName
}

