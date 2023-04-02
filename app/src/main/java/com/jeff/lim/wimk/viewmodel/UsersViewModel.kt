package com.jeff.lim.wimk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.jeff.lim.wimk.database.DBPath
import com.jeff.lim.wimk.database.Users
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor() : ViewModel() {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val auth = Firebase.auth
    private val _authResult = MutableLiveData(false)
    val authResult: LiveData<Boolean>
        get() = _authResult

    // 구글 Firebase 가입을 처리한다.
    // TODO : 추후 카카오톡, 네이버, 구글 계정과 연동한다.
    fun signUp(name: String, email:String, password: String) {
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

                    _authResult.value = true
                } catch (e: Exception) {
                    e.printStackTrace()
                    _authResult.value = false
                }
            } else {
                _authResult.value = false
            }
        }.addOnFailureListener {
            Timber.tag(logTag).d("signUp fail : ${it.message}")
        }
    }

    fun logIn(name: String, email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
            Timber.tag(logTag).d("logIn result : ${authResult.isSuccessful}")

            if (authResult.isSuccessful) {
                Timber.tag(logTag).d("logIn user : ${auth.currentUser}")
                // TODO : Preference 저장 및 자동 로그인 기능 추가
                _authResult.value = true
            } else {
                _authResult.value = false
            }
        }
    }
}