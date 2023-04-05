package com.jeff.lim.wimk.database

data class FamilyModel(
    var familyUID: String? = null,
    val authKeyModel: AuthKeyModel = AuthKeyModel(),
    val users: MutableMap<String, UserModel> = mutableMapOf(),
    val rooms: MutableMap<String, RoomModel> = mutableMapOf()
)