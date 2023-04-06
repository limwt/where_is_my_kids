package com.jeff.lim.wimk.model.service

import com.jeff.lim.wimk.model.Family
import kotlinx.coroutines.flow.StateFlow

interface DatabaseService {
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
    suspend fun getCurrentFamily(): StateFlow<Family?>
    suspend fun updateAuthKey(key: String)
    suspend fun onKidRegister(): StateFlow<Boolean?>
}