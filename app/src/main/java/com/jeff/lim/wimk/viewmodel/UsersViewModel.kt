package com.jeff.lim.wimk.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.jeff.lim.wimk.database.DBPath
import com.jeff.lim.wimk.database.RoleType
import com.jeff.lim.wimk.database.Users
import com.jeff.lim.wimk.database.WimkModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor() : ViewModel() {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val auth = Firebase.auth
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    // 구글 Firebase 가입을 처리한다.
    // TODO : 추후 카카오톡, 네이버, 구글 계정과 연동한다.
    fun signUp(name: String, email:String, password: String, onComplete: (Boolean) -> Unit) {
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
        }
    }

    fun logIn(email: String, password: String, onComplete: (Boolean) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            Timber.tag(logTag).d("logIn result : ${authResult.isSuccessful}")

            if (authResult.isSuccessful) {
                Timber.tag(logTag).d("logIn user : ${auth.currentUser}")
                // TODO : Preference 저장 및 자동 로그인 기능 추가
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }

    fun updateUser(role: String, onComplete: (Boolean) -> Unit) {
        auth.currentUser?.let { user ->
            val wimkModel = WimkModel()
            wimkModel.users[user.uid] = role

            firebaseDatabase.getReference(DBPath.WIMK.path)
                .child(DBPath.WimkRooms.path).push().setValue(wimkModel).addOnCompleteListener {
                    Timber.tag(logTag).d("updateUser success")
                    updateUserRole(role) { result ->
                        onComplete(result)
                    }
                }.addOnFailureListener {
                    Timber.tag(logTag).e("updateUser fail")
                    onComplete(false)
                }


        }
    }

    fun getKidsList() {
        auth.currentUser?.let { user ->

        }
    }

    fun checkRole(onResult: (String) -> Unit) {
        firebaseDatabase.getReference(DBPath.WIMK.path)
            .child(DBPath.Users.path)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        val users = dataSnapshot.getValue(Users::class.java)
                        Timber.tag(logTag).d("checkRole : $users")

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

    private fun updateUserRole(role: String, onComplete: (Boolean) -> Unit) {
        auth.currentUser?.let { user ->
            firebaseDatabase.getReference(DBPath.WIMK.path)
                .child("${DBPath.Users.path}/${user.uid}/${DBPath.Role.path}").setValue(role)
                .addOnCompleteListener {
                    Timber.tag(logTag).d("updateUser role success")
                    onComplete(true)
                }.addOnFailureListener {
                    Timber.tag(logTag).e("updateUser role fail")
                    onComplete(false)
                }
        }
    }
}