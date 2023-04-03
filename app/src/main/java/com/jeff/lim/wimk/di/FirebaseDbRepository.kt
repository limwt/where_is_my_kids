package com.jeff.lim.wimk.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.jeff.lim.wimk.database.DBPath
import com.jeff.lim.wimk.database.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class FirebaseDbRepository {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val auth = Firebase.auth

    suspend fun singUp(name: String, email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            Timber.tag(logTag).d("signUp result : ${authResult.isSuccessful}")

            if (authResult.isSuccessful) {
                try {
                    auth.currentUser?.let { user ->
                        Timber.tag(logTag).d("signUp user : $user")
                        val userId = user.uid
                        FirebaseDatabase.getInstance().getReference(DBPath.WIMK.path).child(DBPath.Users.path)
                            .child(userId).setValue(Users(uid = userId, name = name, email = email))
                    }

                    onComplete(true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    onComplete(false)
                }
            } else {
                onComplete(false)
            }
        }.addOnFailureListener {
            Timber.tag(logTag).d("signUp fail : ${it.message}")
            onComplete(false)
        }
    }


    suspend fun updateUserInfo(user: Users, onComplete: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {

        }
    }
}