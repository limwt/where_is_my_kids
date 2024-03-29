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

    suspend fun register(familyUid: String, name: String, phoneNumber: String, relation: String)
    suspend fun getCurrentFamily(): StateFlow<Family?>
    suspend fun getCurrentFamilyWithAuthKey(key: String): StateFlow<Family?>
    suspend fun getCurrentFamilyWithUid(familyUid: String)
    suspend fun updateAuthKey(uid: String, key: String)
    suspend fun onKidRegister(familyUid: String): StateFlow<Boolean?>
}