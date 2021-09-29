package com.edem.bot.domain

import com.edem.bot.models.MsgStates
import com.edem.bot.services.UserService
import com.edem.bot.services.KabsService
import com.edem.bot.services.NamingAndOrganicService
import com.edem.bot.utills.isKab
import com.edem.bot.utills.isKabEnter
import com.edem.bot.utills.isNaming
import com.edem.bot.utills.isUakKabEnter
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message

@Service
class CommandHandlerUseCase(
    private val kabsService: KabsService,
    private val userService: UserService,
    private val namingAndOrganicService : NamingAndOrganicService
) {
    fun handleText(globalState: MsgStates, message: Message, bot : TelegramLongPollingBot) : MsgStates{
        val msgText = message.text
        return when {
            //todo кабинеты через запятую
            msgText == "/start" -> handleAuth(message.chatId)
            (msgText.isKab() || msgText.isKabEnter()) && (globalState is MsgStates.AddAdsIdState) -> {
                kabsService.addKabs(msgText, message.chatId, (globalState as MsgStates.AddAdsIdState).appId)
                MsgStates.Successful("Добавление кабинетов")
            }
            msgText.isUakKabEnter() && (globalState is MsgStates.AddUAKIdState) -> {
                kabsService.addUAKKabs(msgText, message.chatId, (globalState as MsgStates.AddUAKIdState).appId)
                MsgStates.Successful("Добавление кабинетов")
            }
            msgText.isNaming() && (globalState is MsgStates.CreateNamingState) -> {
                bot.execute(SendMessage().setChatId(message.chatId).setText(namingAndOrganicService.createNaming(msgText)))
                MsgStates.Successful("Создание нейминга")
            }
            msgText.startsWith("/addApp")-> {
                handleAddApp(message.chatId, msgText)
            }
            msgText.startsWith("/getToken")-> {
                handleToken(message.chatId, msgText, bot)
            }
            else -> MsgStates.Error
        }
    }

    private fun handleToken(chatId: Long, msgText : String, bot: TelegramLongPollingBot) : MsgStates{
        val user = userService.findUser(chatId)
        return if(user.userGroup == "admin"){
            val appToken = msgText.replace("/getToken", "")
                .replace("e", "")
                .replace(" ", "").toInt() shl 20
            val responseMsg = "App token is $appToken"
            bot.execute(SendMessage().setChatId(chatId).setText(responseMsg))
            MsgStates.StartState(userService.findAppsForGroup(chatId)!!)
        } else {
            MsgStates.AuthState
        }
    }

    private fun handleAddApp(chatId: Long, msgText : String) : MsgStates{
        val user = userService.findUser(chatId)
        return if(user.userGroup == "guest"){
            val appToken = msgText.replace("/addApp", "").replace(" ", "").toInt() shr 20
            val appIde = "e${appToken}"
            userService.addAppToUser(appIde, chatId)
            MsgStates.StartState(userService.findAppsForGroup(chatId))
        } else {
            MsgStates.AuthState
        }
    }

    private fun handleAuth(chatId : Long) : MsgStates {
        val user = userService.findUser(chatId)
        return when(user.userGroup){
            null -> {
                userService.addUser(chatId)
                MsgStates.AuthState
            }
            "wait" -> {
                MsgStates.AuthState
            }
            else -> {
                MsgStates.StartState(userService.findAppsForGroup(chatId)!!)
            }
        }

    }
}