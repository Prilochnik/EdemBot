package com.edem.bot

import com.edem.bot.domain.CommandHandlerUseCase
import com.edem.bot.domain.StateHandlerUseCase
import com.edem.bot.models.MsgStates
import com.edem.bot.services.AppService
import com.edem.bot.models.Button
import com.edem.bot.services.KabsService
import com.edem.bot.services.NamingAndOrganicService
import com.edem.bot.services.UserService
import com.edem.bot.utills.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText
import org.telegram.telegrambots.meta.api.objects.Update

@Component
class EdemBot(
        private val appService : AppService,
        private val userService: UserService,
        private val stateHandlerUseCase: StateHandlerUseCase,
        private val commandHandlerUseCase: CommandHandlerUseCase
) : TelegramLongPollingBot() {

    @Value("\${bot.name}")
    private val botName : String = ""

    @Value("\${bot.token}")
    private val token : String = ""

    var globalState : MsgStates = MsgStates.AuthState

    override fun onUpdateReceived(update: Update) {
        if(update.hasMessage()){
            val message = update.message
            val apps = appService.getApps()
            println(apps)
            val chatId = message.chatId
            val responseState = if (message.hasText()) {
                commandHandlerUseCase.handleText(globalState, message, this) //КОСТЫЛЬ, надо ебнуть коллбек
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
                msgText == Button.UACButtonText -> MsgStates.AddUAKIdState((globalState as MsgStates.AppChosenState).appId)
                msgText == Button.cancel -> MsgStates.StartState(userService.findAppsForGroup(chatId)!!)
                msgText == Button.BACK -> MsgStates.StartState(userService.findAppsForGroup(chatId)!!)
                else -> MsgStates.Error
            }
            sendInlineMsg(chatId, responseState, update.callbackQuery.message.messageId) //todo needs to update msg
        }
    }

    fun sendInlineMsg(chatId: Long, state : MsgStates, msgId : Int) {
        globalState = state
        println(globalState)
        val responseModel = stateHandlerUseCase.handleState(chatId, state)
        val responseMessage = EditMessageReplyMarkup()
                .setMessageId(msgId)
                .setChatId(chatId)
        val responseTextMsg = EditMessageText()
                .setMessageId(msgId)
                .setChatId(chatId)
                .setText(responseModel.msg)
        responseMessage.replyMarkup = Keyboard.getInlineMarkup(responseModel.buttons)
        execute(responseTextMsg)
        execute(responseMessage)


    }

    override fun getBotToken(): String = token

    override fun getBotUsername(): String = botName
}

//fun main(){
//    val id = "47"
//    val key = id.toInt() shl 20
//    val deKey = key shr 20
//    print(deKey)
//}
