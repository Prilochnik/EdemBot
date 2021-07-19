package com.edem.bot

import com.edem.bot.models.MsgStates
import com.edem.bot.models.MsgText
import com.edem.bot.models.ResponseModel
import com.edem.bot.services.AppService
import com.edem.bot.models.Button
import com.edem.bot.repos.KabsRepository
import com.edem.bot.services.KabsService
import com.edem.bot.utills.Keyboard
import com.edem.bot.utills.isInteger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update

@Service
class EdemBot(
        private val appService : AppService,
        private val kabsService: KabsService
) : TelegramLongPollingBot() {

    @Value("\${bot.name}")
    private val botName : String = ""

    @Value("\${bot.token}")
    private val token : String = ""

    var globalState : MsgStates = MsgStates.StartState

    override fun onUpdateReceived(update: Update) {
        if(update.hasMessage()){
            val message = update.message
            val apps = appService.getApps()
            println(apps)
            val chatId = message.chatId
            val responseState = if (message.hasText()) {
                val msgText = message.text
                when {
                    //todo кабинеты через запятую
                    msgText == "/start" -> MsgStates.StartState
                    msgText.isInteger() && (globalState is MsgStates.AddAdsIdState) -> {
                        kabsService.addKab(msgText, message.chatId, (globalState as MsgStates.AddAdsIdState).appId)
                        MsgStates.Successful("Добавление кабинетов")
                    }
                    else -> MsgStates.Error
                }
            } else {
                MsgStates.Error
            }
            val responseMessage = SendMessage(chatId,"Hi" )
            responseMessage.setParseMode("Markdown")
            val a = execute(responseMessage)
            sendInlineMsg(chatId, responseState, a.messageId)
        }
        else if(update.hasCallbackQuery()) {
            val msgText = update.callbackQuery.data
            val chatId = update.callbackQuery.message.chatId
            val apps = appService.getApps().map { "${it.appIde} ${it.appName}"}
            val responseState = when {
                apps.contains(msgText) -> MsgStates.AppChosenState(msgText)
                msgText == Button.addAdsButtonText -> MsgStates.AddAdsIdState((globalState as MsgStates.AppChosenState).appId)
                msgText == Button.namingButtonText -> MsgStates.CreateNamingState((globalState as MsgStates.AppChosenState).appId)
                msgText == Button.organicButtonText -> MsgStates.AddOrganicState((globalState as MsgStates.AppChosenState).appId)
                msgText == Button.cancel -> MsgStates.StartState
                msgText == Button.BACK -> MsgStates.StartState
                else -> MsgStates.Error
            }
            sendInlineMsg(chatId, responseState, update.callbackQuery.message.messageId) //todo needs to update msg
        }
    }



    fun sendInlineMsg(chatId: Long, state : MsgStates, msgId : Int) {
        globalState = state
        println(globalState)
        val responseModel = when(state) {
            is MsgStates.StartState -> {
                //responseText = "Ну приветики, на какую прилу желаешь залится?"
                ResponseModel(
                        chatId = chatId,
                        msg = MsgText.CHOOSE_APP,//"Ну приветики, на какую прилу желаешь залится?",
                        buttons = appService.getAppsFormat()
                )
            }
            is MsgStates.AppChosenState -> {
                ResponseModel(
                        chatId = chatId,
                        msg = MsgText.appChosen(state.appId),
                        buttons = listOf(listOf(Button.addAdsButtonText), listOf(Button.namingButtonText), listOf(Button.organicButtonText), listOf(Button.BACK))
                )
            }
            is MsgStates.AddAdsIdState ->{
                ResponseModel(
                        chatId = chatId,
                        msg = MsgText.ADD_ADS_ID,
                        buttons = listOf(listOf(Button.cancel))
                )
            }
            is MsgStates.AddOrganicState -> {
                ResponseModel(
                        chatId = chatId,
                        msg = MsgText.ORGANIC,
                        buttons = listOf(listOf(Button.cancel))
                )
            }
            is MsgStates.CreateNamingState -> {
                ResponseModel(
                        chatId = chatId,
                        msg = MsgText.CREATE_NAMING,
                        buttons = listOf(listOf(Button.cancel))
                )
            }
            is MsgStates.RemoveAdsIdState -> {
                ResponseModel(
                        chatId = chatId,
                        msg = MsgText.REMOVE_AD_ID,
                        buttons = listOf( listOf(Button.cancel))
                )
            }
            is MsgStates.Error -> {
                ResponseModel(
                        chatId = chatId,
                        msg = MsgText.ERROR,
                        buttons = listOf( listOf(Button.cancel))
                )
            }
            is MsgStates.Successful -> {
                ResponseModel(
                        chatId = chatId,
                        msg = MsgText.successful(state.option),
                        buttons = listOf(listOf(Button.BACK))
                )
            }
        }
        //val responseMessage = SendMessage(chatId, responseModel.msg)
        val responseMessage = EditMessageReplyMarkup()
                .setMessageId(msgId)
                .setChatId(chatId)
        val responseTextMsg = EditMessageText()
                .setMessageId(msgId)
                .setChatId(chatId)
                .setText(responseModel.msg)
       // responseMessage.setParseMode("Markdown")
        responseMessage.replyMarkup = Keyboard.getInlineMarkup(responseModel.buttons)
//        responseMessage.replyMarkup.keyboard.forEach { lits ->
//            lits.forEach {
//                it.setText(responseModel.buttons.toMutableList().removeLast()[0])
//            }
//        }
        execute(responseTextMsg)
        execute(responseMessage)


    }

    override fun getBotToken(): String = token

    override fun getBotUsername(): String = botName
}
