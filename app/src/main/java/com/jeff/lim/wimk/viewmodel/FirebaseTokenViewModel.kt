package com.jeff.lim.wimk.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.jeff.lim.wimk.database.DataBaseInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class FirebaseTokenViewModel @Inject constructor(@ApplicationContext private val context: Context) : ViewModel() {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val _tokenData = MutableLiveData<Any>(null)
    val getTokenData: LiveData<Any?> get() = _tokenData
    private val androidId = MutableLiveData("")

    fun updateAndroidId(id: String) {
        androidId.value = id
    }

    fun requestToken(relation: String, phoneNumber: String) {
        var result = true
        val path = "${DataBaseInfo.WIMK.name}/$relation/${androidId.value}/${DataBaseInfo.Tokens.name}"

        Firebase.database.getReference(path).get().addOnSuccessListener {
            Timber.tag(logTag).d("getToken from Firebase database : ${it.value}")
            val data: Any? = it.value

            if (data == null) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Timber.tag(logTag).d("registerTokenManager - Fail : ${task.exception?.message}")
                    } else {
                        sendRegistrationToServer(path, task.result ?: "")
                    }
                }
            } else {
                result = false
            }
        }.addOnFailureListener {
            Timber.tag(logTag).d("getToken from Firebase database - Fail : $it")
        }
    }

    /**
     * 서버에 토큰값을 저장합니다.
     * document = 디바이스 고유 ID ( UniqueID )
     */
    private fun sendRegistrationToServer(path: String, token: String) {
        Timber.tag(logTag).d("sendRegistrationToServer")
        Firebase.database.getReference(path).apply {
            setValue(token)
        }
    }
}