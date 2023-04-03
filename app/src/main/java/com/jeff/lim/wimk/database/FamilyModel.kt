package com.jeff.lim.wimk.database

data class FamilyModel(
    var auth: String? = null,
    val users: MutableMap<String, UserModel> = mutableMapOf(),
    val rooms: MutableMap<String, RoomModel> = mutableMapOf()
)