package com.jeff.lim.wimk.database

data class WimkModel(
    // Key: User 의 uid
    // Value : role
    val users: MutableMap<String, String> = mutableMapOf(),
    // Key : Command uid
    // Value - first : cmd
    // Value - second : data
    val commands: MutableMap<String, Pair<String, String>> = mutableMapOf(),
    // Key : Chat uid
    // Value : Chat data
    val chat: MutableMap<String, Chat> = mutableMapOf()
)