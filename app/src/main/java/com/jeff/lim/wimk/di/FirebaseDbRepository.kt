package com.jeff.lim.wimk.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.jeff.lim.wimk.database.DBPath
import com.jeff.lim.wimk.database.RoleType
import com.jeff.lim.wimk.database.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import kotlin.random.Random

class FirebaseDbRepository {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val auth = Firebase.auth
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val certLength = 6
    private val charTable = charArrayOf(
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
        'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
        'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'
    )

    suspend fun singUp(key: String, name: String, email: String, password: String, onComplete: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                Timber.tag(logTag).d("signUp result : ${authResult.isSuccessful}")

                if (authResult.isSuccessful) {
                    try {
                        auth.currentUser?.let { user ->
                            Timber.tag(logTag).d("signUp user : $user")
                            val userId = user.uid
                            val authKey = key.ifEmpty { generateAuthKey() }
                            FirebaseDatabase.getInstance().getReference(DBPath.WIMK.path).child(DBPath.Family.path)
                                .child(authKey)
                                .child(DBPath.Users.path)
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
    }

    suspend fun logIn(email: String, password: String, onComplete: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                Timber.tag(logTag).d("logIn result : ${authResult.isSuccessful}")

                if (authResult.isSuccessful) {
                    Timber.tag(logTag).d("logIn user : ${auth.currentUser}")
                    onComplete(true)
                } else {
                    onComplete(false)
                }
            }
        }
    }

    suspend fun checkRole(onResult: (String) -> Unit) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->
                firebaseDatabase.getReference(DBPath.WIMK.path)
                    .child(DBPath.Family.path)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot in snapshot.children) {
                                val users = dataSnapshot.child(DBPath.Users.path).child(user.uid).getValue(Users::class.java)

                                if (users != null) {
                                    onResult(users.role)
                                } else {
                                    onResult(RoleType.Init.role)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            onResult(RoleType.Init.role)
                        }
                    })
            }
        }
    }


    suspend fun updateUserInfo(user: Users, onComplete: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {

        }
    }

    private fun generateAuthKey(): String {
        val random = Random(charTable.size)
        val result = StringBuilder()

        for (i in 0 until certLength) {
            result.append(charTable[random.nextInt(charTable.size)])
        }

        return result.toString()
    }
}