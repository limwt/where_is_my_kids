package com.jeff.lim.wimk.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.jeff.lim.wimk.database.DBPath
import com.jeff.lim.wimk.database.FamilyModel
import com.jeff.lim.wimk.database.RelationType
import com.jeff.lim.wimk.database.UserModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import timber.log.Timber

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

    private val familyModel = FamilyModel()


    suspend fun singUp(email: String, password: String): StateFlow<Boolean?> {
        val result = MutableStateFlow<Boolean?>(null)

        withContext(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                Timber.tag(logTag).d("signUp result>>>> : ${authResult.isSuccessful}")
                result.value = authResult.isSuccessful
            }.addOnFailureListener {
                Timber.tag(logTag).d("signUp fail : ${it.message}")
                result.value = false
            }
        }

        return result.asStateFlow()
    }

    suspend fun logIn(email: String, password: String): StateFlow<Boolean?> {
        val result = MutableStateFlow<Boolean?>(null)

        withContext(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { authResult ->
                    Timber.tag(logTag).d("logIn result : ${authResult.isSuccessful}")
                    result.value = authResult.isSuccessful
                }
                .addOnFailureListener {
                    Timber.tag(logTag).d("logIn fail : ${it.message}")
                    result.value = false
                }
        }

        return result.asStateFlow()
    }

    suspend fun checkFamilyRoom(): StateFlow<FamilyModel?> {
        val result = MutableStateFlow<FamilyModel?>(null)

        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->
                firebaseDatabase.getReference(DBPath.WIMK.path).child(DBPath.Family.path)
                    .orderByChild("users/${user.uid}")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.value != null) {
                                dataSnapshotLoop@ for (dataSnapshot in snapshot.children) {
                                    Timber.tag(logTag).d("checkFamilyRoom - onDataChange : ${dataSnapshot.key} ${dataSnapshot.value}")
                                    Timber.tag(logTag).d("checkFamilyRoom - onRealDataChange : ${dataSnapshot.getValue(FamilyModel::class.java)}")
                                    val family = dataSnapshot.getValue(FamilyModel::class.java)

                                    if (family != null) {
                                        for (u in family.users) {
                                            if (u.key == user.uid) {
                                                familyModel.familyUID = dataSnapshot.key
                                                familyModel.users.putAll(family.users.toMutableMap())
                                                familyModel.rooms.putAll(family.rooms.toMutableMap())
                                                result.value = familyModel
                                                break@dataSnapshotLoop
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Timber.tag(logTag).d("checkFamilyRoom - onCancelled: ${error.message}")
                        }
                    })
            }
        }

        return result.asStateFlow()
    }

    suspend fun checkRole(onResult: (String) -> Unit) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->
                firebaseDatabase.getReference(DBPath.WIMK.path)
                    .child(DBPath.Family.path)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot in snapshot.children) {
                                val userModel = dataSnapshot.child(DBPath.Users.path).child(user.uid).getValue(UserModel::class.java)

                                if (userModel != null) {
                                    onResult(userModel.role)
                                } else {
                                    onResult(RelationType.Init.relation)
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            onResult(RelationType.Init.relation)
                        }
                    })
            }
        }
    }

    suspend fun updateUser(familyUid: String, name: String, role: String): StateFlow<Boolean?> {
        val result = MutableStateFlow<Boolean?>(null)

        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->
                Timber.tag(logTag).d("updateUser : $user, $familyUid")
                if (familyUid.isNotEmpty()) {
                    val userModel = UserModel(uid = user.uid, name = name, role = role, email = user.email!!)
                    familyModel.users.set(user.uid, userModel)
                    firebaseDatabase.getReference(DBPath.WIMK.path)
                        .child("${DBPath.Family.path}/$familyUid")
                        .child("${DBPath.Users.path}/${user.uid}")
                        .setValue(userModel)
                        .addOnCompleteListener {
                            result.value = it.isSuccessful
                        }
                        .addOnFailureListener {
                            result.value = false
                        }
                } else {
                    familyModel.users[user.uid] = UserModel(uid = user.uid, name = name, role = role, email = user.email!!)

                    firebaseDatabase.getReference(DBPath.WIMK.path)
                        .child(DBPath.Family.path)
                        .push()
                        .setValue(familyModel)
                        .addOnCompleteListener {
                            result.value = it.isSuccessful
                        }
                        .addOnFailureListener {
                            result.value = false
                        }
                }
            }
        }

        return result.asStateFlow()
    }

    suspend fun getUserInfo(): StateFlow<Boolean?> {
        val result = MutableStateFlow<Boolean?>(null)
        withContext(Dispatchers.IO) {
            auth.currentUser?.let {
                firebaseDatabase.getReference(DBPath.WIMK.path)
                    .child("${DBPath.Family.path}/${familyModel.familyUID}")
                    .child(DBPath.Users.path)
                    .addChildEventListener(object : ChildEventListener {
                        override fun onChildAdded(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            Timber.tag(logTag).d("onChildAdded: ${snapshot.value}")
                            // 자녀 추가...
                            snapshot.getValue(UserModel::class.java)?.let { user ->
                                if (user.role == RelationType.Son.relation || user.role == RelationType.Daughter.relation) {
                                    result.value = true
                                }
                            }
                        }

                        override fun onChildChanged(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            Timber.tag(logTag).d("onChildChanged: ${snapshot.value}")
                        }

                        override fun onChildRemoved(snapshot: DataSnapshot) {
                        }

                        override fun onChildMoved(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
            }
        }

        return result
    }

    suspend fun getFamilyUID(authKey: String): StateFlow<String?> {
        val result = MutableStateFlow<String?>(null)

        withContext(Dispatchers.IO) {
            auth.currentUser?.let {
                firebaseDatabase.getReference(DBPath.WIMK.path)
                    .child(DBPath.Family.path)
                    .orderByChild(DBPath.Auth.path).equalTo(authKey)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot in snapshot.children) {
                                Timber.tag(logTag)
                                    .d("getFamilyUID - onDataChange : ${dataSnapshot.key}, ${dataSnapshot.value}")
                                val familyUID = dataSnapshot.key!!
                                result.value = familyUID
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            }
        }

        return result.asStateFlow()
    }

    suspend fun updateAuthKey(key: String) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.let {
                firebaseDatabase.getReference(DBPath.WIMK.path)
                    .child("${DBPath.Family.path}/${familyModel.familyUID}")
                    .child(DBPath.Auth.path)
                    .setValue(key)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Timber.tag(logTag).d("updateAuthKey - Success")
                        } else {
                            Timber.tag(logTag).e("updateAuthKey - Fail")
                        }
                    }
                    .addOnFailureListener {
                        Timber.tag(logTag).e("updateAuthKey - Fail")
                    }
            }
        }
    }

    private fun getRandomString() : String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..8)
            .map { charset.random() }
            .joinToString("")
    }
}