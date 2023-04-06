package com.jeff.lim.wimk.model.service

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.jeff.lim.wimk.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean
    val currentUser: Flow<User>

    suspend fun logIn(email: String, password: String): Task<AuthResult>
    suspend fun signUp(email: String, password: String)
}