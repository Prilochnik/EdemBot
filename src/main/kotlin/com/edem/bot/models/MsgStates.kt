package com.edem.bot.models

sealed class MsgStates {
    object StartState : MsgStates()
    class AppChosenState(val appId : String) : MsgStates()
    class AddAdsIdState(val appId : String) : MsgStates()
    class CreateNamingState(val appId : String) : MsgStates()
    class AddOrganicState(val appId : String) : MsgStates()
    class RemoveAdsIdState(val appId : String) : MsgStates()
    class Successful(val option : String) : MsgStates()
    object Error : MsgStates()
}


