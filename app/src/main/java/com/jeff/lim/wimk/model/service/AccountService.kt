package com.jeff.lim.wimk.model.service

import com.jeff.lim.wimk.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>

    suspend fun logIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String)
}