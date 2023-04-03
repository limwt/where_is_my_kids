package com.jeff.lim.wimk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.jeff.lim.wimk.database.DBPath
import com.jeff.lim.wimk.database.WimkModel
import com.jeff.lim.wimk.di.FirebaseDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val repository: FirebaseDbRepository
) : ViewModel() {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val auth = Firebase.auth
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val _familyRoomUID = MutableLiveData<String>(null)
    val familyRoomUID: LiveData<String>
        get() = _familyRoomUID

    // 구글 Firebase 가입을 처리한다.
    // TODO : 추후 카카오톡, 네이버, 구글 계정과 연동한다.
    fun signUp(email:String, password: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.singUp(email, password, onComplete)
        }
    }

    fun logIn(email: String, password: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.logIn(email, password, onComplete)
        }
    }

    fun updateUser(role: String, onComplete: (Boolean) -> Unit) {
        auth.currentUser?.let { user ->
            val wimkModel = WimkModel()
            wimkModel.users[user.uid] = role

            // TODO : DB 에 부모 정보를 확인할 수 있어야 한다.
            // 부모 정보의 uid 를 이용해서 wimkRoomKey 를 얻고...
            // 동일한 Room 에 저장한다.
            // 부모 화면에서 자녀 추가시 인증번호를 생성하여 DB 에 저장하고...
            // 자녀화면에서 인증 번호를 기반으로 부모 uid 를 얻어 온다.


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
        viewModelScope.launch {
            repository.checkRole(onResult)
        }
    }

    fun checkFamilyRoom(onComplete: (Pair<String?, String?>) -> Unit) {
        viewModelScope.launch {
            repository.checkFamilyRoom(onComplete)
        }
    }

    fun updateUser(name: String, role: String, onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            repository.updateUser(name, role, onComplete)
        }
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