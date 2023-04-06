package com.jeff.lim.wimk.model.service

import com.jeff.lim.wimk.model.Family
import kotlinx.coroutines.flow.Flow

interface DatabaseService {
    val currentFamily: Flow<Family>

    enum class Path(val path: String) {
        WIMK("wimk"),
        Users("users"),
        WimkRooms("wimkRooms"),
        Role("role"),
        Family("family"),
        Auth("auth"),
        WimkRoomKey("")
    }

    suspend fun register(name: String, phoneNumber: String, relation: String)
}