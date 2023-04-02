package com.jeff.lim.wimk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.jeff.lim.wimk.database.DBPath
import com.jeff.lim.wimk.database.WimkModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WimkViewModel @Inject constructor() : ViewModel() {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val auth = Firebase.auth
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val _updateUser = MutableLiveData(false)
    val updateUser: LiveData<Boolean>
        get() = _updateUser

    fun updateUser(role: String) {
        auth.currentUser?.let { user ->
            val map = mutableMapOf<String, String>()
            map[user.uid] = role
            firebaseDatabase.getReference(DBPath.WIMK.path)
                .child(DBPath.WimkRooms.path)
                .child(DBPath.WimkRoomKey.path)
                .setValue(WimkModel(users = map)).addOnCompleteListener {
                    _updateUser.value = true
                }.addOnFailureListener {
                    _updateUser.value = false
                }
        }
    }
}