package com.edem.bot.domain

import com.edem.bot.models.Button
import com.edem.bot.models.MsgStates
import com.edem.bot.models.MsgText
import com.edem.bot.models.ResponseModel
import com.edem.bot.services.AppService
import org.springframework.stereotype.Service

@Service
class StateHandlerUseCase(
    private val appService: AppService
) {
    fun handleState(chatId: Long, state : MsgStates): ResponseModel {
        return when(state) {
            is MsgStates.AuthState -> {
                ResponseModel(
                    chatId = chatId,
                    msg = MsgText.authToken(chatId.toString()),
                    buttons = listOf(listOf())
                )
            }
            is MsgStates.StartState -> {
                //responseText = "Ну приветики, на какую прилу желаешь залится?"
                ResponseModel(
                    chatId = chatId,
                    msg = MsgText.CHOOSE_APP,//"Ну приветики, на какую прилу желаешь залится?",
                    buttons = try {
                        appService.formatAppsForUser(state.appsId)
                    } catch (e : Exception){
                        listOf(listOf())
                    }
                )
            }
            is MsgStates.AppChosenState -> {
                ResponseModel(
                    chatId = chatId,
                    msg = MsgText.appChosen(state.appId, appService.createLink(state.appId)),
                    buttons = listOf(
                        listOf(Button.addAdsButtonText),
                        listOf(Button.UACButtonText),
                        listOf(Button.namingButtonText),
                        listOf(Button.organicButtonText),
                        listOf(Button.BACK)
                    )
                )
            }
            is MsgStates.AddAdsIdState ->{
                ResponseModel(
                    chatId = chatId,
                    msg = MsgText.ADD_ADS_ID,
                    buttons = listOf(listOf(Button.cancel))
                )
            }
            is MsgStates.AddUAKIdState -> {
                ResponseModel(
                    chatId = chatId,
                    msg = MsgText.UAK_ADS,
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
    }
}