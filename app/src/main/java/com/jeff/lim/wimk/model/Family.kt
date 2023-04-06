package com.jeff.lim.wimk.model

import com.jeff.lim.wimk.database.AuthKeyModel
import com.jeff.lim.wimk.database.RoomModel

data class Family(
    val familyUid: String? = null,
    val authKeyModel: AuthKeyModel = AuthKeyModel(),
    val users: MutableMap<String, User> = mutableMapOf(),
    val rooms: MutableMap<String, RoomModel> = mutableMapOf()
)
