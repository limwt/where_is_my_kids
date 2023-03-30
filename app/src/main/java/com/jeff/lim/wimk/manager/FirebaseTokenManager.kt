package com.jeff.lim.wimk.manager

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Secure
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.jeff.lim.wimk.database.DataBaseInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseTokenManager @Inject constructor(@ApplicationContext private val context: Context) {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val path = DataBaseInfo.WIMK.name

    var token: String = ""

    /**
     * 해당 디바이스의 Token을 받아옵니다.
     */
    fun registerTokenManager(): Boolean {
        var result = true

        Firebase.database.getReference(path).get().addOnSuccessListener {
            Timber.tag(logTag).d("getToken from Firebase database : ${it.value}")
            val data: Any? = it.value

            if (data == null) {
                FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Timber.tag(logTag).d("registerTokenManager - Fail : ${task.exception?.message}")
                    } else {
                        sendRegistrationToServer(task.result ?: "")
                    }
                }
            } else {
                result = false
            }
        }.addOnFailureListener {
            Timber.tag(logTag).d("getToken from Firebase database - Fail : $it")
        }

        return result
    }

    /**
     * 서버에 토큰값을 저장합니다.
     * document = 디바이스 고유 ID ( UniqueID )
     */
    private fun sendRegistrationToServer(token: String) {
        Timber.tag(logTag).d("sendRegistrationToServer")
        Firebase.database.getReference(path).apply {
            setValue(token)
        }
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId() = Secure.getString(context.contentResolver, Secure.ANDROID_ID)

}