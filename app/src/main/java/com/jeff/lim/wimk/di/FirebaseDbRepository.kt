package com.jeff.lim.wimk.di

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.jeff.lim.wimk.database.DBPath
import com.jeff.lim.wimk.database.FamilyModel
import com.jeff.lim.wimk.database.RoleType
import com.jeff.lim.wimk.database.UserModel
import kotlinx.coroutines.Dispatchers
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

    private var familyUID: String? = null
    private var familyModel: FamilyModel? = null

    suspend fun singUp(email: String, password: String, onComplete: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { authResult ->
                Timber.tag(logTag).d("signUp result : ${authResult.isSuccessful}")

                if (authResult.isSuccessful) {
                    onComplete(true)
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
                                val userModel = dataSnapshot.child(DBPath.Users.path).child(user.uid).getValue(UserModel::class.java)

                                if (userModel != null) {
                                    onResult(userModel.role)
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

    suspend fun checkFamilyRoom(onComplete: (Pair<String?, String?>) -> Unit) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->
                firebaseDatabase.getReference(DBPath.WIMK.path).child(DBPath.Family.path)
                    .orderByChild("users/${user.uid}")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            Timber.tag(logTag).d("checkFamilyRoom - onDataChange : ${snapshot.value}")

                            if (snapshot.value == null) {
                                onComplete(Pair(familyUID, RoleType.Init.role))
                            } else {
                                for (dataSnapshot in snapshot.children) {
                                    familyModel = dataSnapshot.getValue(FamilyModel::class.java)
                                    Timber.tag(logTag).d("checkFamilyRoom - familyModel: $familyModel")
                                    var role = RoleType.Init.role

                                    familyModel?.let { model ->
                                        var matched = false

                                        for (u in model.users) {
                                            if (u.key == user.uid) {
                                                familyUID = dataSnapshot.key
                                                role = model.users[user.uid]?.role ?: RoleType.Init.role
                                                matched = true
                                                break
                                            }
                                        }

                                        if (matched) {
                                            onComplete(Pair(familyUID, role))
                                        } else {
                                            onComplete(Pair(familyUID, RoleType.Init.role))
                                        }
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            onComplete(Pair(null, null))
                        }
                    })
            }
        }
    }

    suspend fun updateUser(familyUid: String, name: String, role: String, onComplete: (Boolean) -> Unit) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->
                Timber.tag(logTag).d("updateUser : $user, $familyUid")
                val userModel = UserModel(uid = user.uid, name = name, role = role, email = user.email!!)

                if (familyUid.isNotEmpty()) {
                    familyModel?.users?.set(user.uid, userModel)

                    firebaseDatabase.getReference(DBPath.WIMK.path)
                        .child("${DBPath.Family.path}/$familyUid")
                        .child("${DBPath.Users.path}/${user.uid}")
                        .setValue(userModel)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                onComplete(true)
                            } else {
                                onComplete(false)
                            }
                        }
                        .addOnFailureListener {
                            onComplete(false)
                        }
                } else {
                    val userMap = mutableMapOf<String, UserModel>()
                    userMap[user.uid] = userModel
                    familyModel = FamilyModel(users = userMap)

                    firebaseDatabase.getReference(DBPath.WIMK.path)
                        .child(DBPath.Family.path)
                        .push()
                        .setValue(familyModel)
                        .addOnCompleteListener {
                            if (it.isSuccessful) {
                                onComplete(true)
                            } else {
                                onComplete(false)
                            }
                        }
                        .addOnFailureListener {
                            onComplete(false)
                        }
                }
            }
        }
    }

    suspend fun getUserInfo() {
        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->
                firebaseDatabase.getReference(DBPath.WIMK.path)
                    .child("${DBPath.Family.path}/$familyUID")
                    .child(DBPath.Users.path)
                    .addChildEventListener(object : ChildEventListener{
                        override fun onChildAdded(
                            snapshot: DataSnapshot,
                            previousChildName: String?
                        ) {
                            Timber.tag(logTag).d("onChildAdded: ${snapshot.value}")
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
    }

    suspend fun getFamilyUID(authKey: String, onComplete: (String) -> Unit) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->
                firebaseDatabase.getReference(DBPath.WIMK.path)
                    .child(DBPath.Family.path)
                    .orderByChild(DBPath.Auth.path).equalTo(authKey)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (dataSnapshot in snapshot.children) {
                                Timber.tag(logTag).d("getFamilyUID - onDataChange : ${dataSnapshot.key}, ${dataSnapshot.value}")
                                val familyUID = dataSnapshot.key!!
                                onComplete(familyUID)
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            onComplete("")
                        }
                    })
            }
        }
    }

    suspend fun updateAuthKey(key: String) {
        withContext(Dispatchers.IO) {
            auth.currentUser?.let { user ->
                firebaseDatabase.getReference(DBPath.WIMK.path)
                    .child("${DBPath.Family.path}/$familyUID")
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