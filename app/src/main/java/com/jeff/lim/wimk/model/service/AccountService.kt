package com.jeff.lim.wimk.model.service

interface AccountService {
    val currentUserId: String
    val currentUserEmail: String
    val hasUser: Boolean

    suspend fun logIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String)
}