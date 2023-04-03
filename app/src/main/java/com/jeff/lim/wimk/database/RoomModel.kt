package com.jeff.lim.wimk.database

data class RoomModel(
    val users: MutableMap<String, Boolean> = mutableMapOf(),
    val commands: MutableMap<String, CmdModel> = mutableMapOf(),
    val chatModel: MutableMap<String, ChatModel> = mutableMapOf()
)