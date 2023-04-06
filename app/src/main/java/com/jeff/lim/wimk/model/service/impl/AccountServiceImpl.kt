package com.jeff.lim.wimk.model.service.impl

import com.google.firebase.auth.FirebaseAuth
import com.jeff.lim.wimk.model.service.AccountService
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(private val auth: FirebaseAuth) : AccountService {
    private val logTag = "[WIMK]AccountService"

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val currentUserEmail: String
        get() = auth.currentUser?.email.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override suspend fun logIn(email: String, password: String): Boolean {
        var result = false
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                Timber.tag(logTag).d("logIn ${it.isSuccessful}")
                result = it.isSuccessful
            }
            .addOnFailureListener {
                result = false
            }
            .await()

        return result
    }

    override suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }
}