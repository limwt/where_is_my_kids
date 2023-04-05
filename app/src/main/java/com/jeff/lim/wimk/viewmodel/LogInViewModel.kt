package com.jeff.lim.wimk.viewmodel

import androidx.lifecycle.ViewModel
import com.jeff.lim.wimk.di.FirebaseDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val repository: FirebaseDbRepository
) : ViewModel() {
    private val logTag = "[WIMK]${this::class.java.simpleName}"

    // 구글 Firebase 가입을 처리한다.
    // TODO : 추후 카카오톡, 네이버, 구글 계정과 연동한다.
    suspend fun signUp(email:String, password: String): StateFlow<Boolean?> {
        Timber.tag(logTag).d("signUp ($email, $password)")
        return repository.singUp(email, password)
    }

    suspend fun logIn(email: String, password: String): StateFlow<Boolean?> {
        return repository.logIn(email, password)
    }
}