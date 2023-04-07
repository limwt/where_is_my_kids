package com.jeff.lim.wimk.model.service.impl

import com.google.firebase.database.*
import com.jeff.lim.wimk.database.RelationType
import com.jeff.lim.wimk.model.DBPath
import com.jeff.lim.wimk.model.Family
import com.jeff.lim.wimk.model.User
import com.jeff.lim.wimk.model.service.AccountService
import com.jeff.lim.wimk.model.service.DatabaseService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class DatabaseServiceImpl @Inject constructor(
    private val auth: AccountService,
    private val database: FirebaseDatabase
) : DatabaseService {
    private val logTag = "[WIMK]DatabaseService"

    override suspend fun register(
        familyUid: String,
        name: String,
        phoneNumber: String,
        relation: String
    ) {
        if (familyUid.isEmpty()) {
            val data = mutableMapOf<String, User>()
            data[auth.currentUserId] = User(
                id = auth.currentUserId,
                name = name,
                relation = relation,
                phoneNumber = phoneNumber,
                email = auth.currentUserEmail
            )

            database.getReference(DBPath.WIMK.path)
                .child(DBPath.Family.path)
                .push()
                .setValue(Family(users = data)).await()
        } else {
            database.getReference(DBPath.WIMK.path)
                .child("${DBPath.Family.path}/$familyUid")
                .child("${DBPath.Users.path}/${auth.currentUserId}")
                .setValue(User(
                    id = auth.currentUserId,
                    name = name,
                    relation = relation,
                    phoneNumber = phoneNumber,
                    email = auth.currentUserEmail
                )).await()
        }
    }

    override suspend fun getCurrentFamily(): StateFlow<Family?> {
        val result = MutableStateFlow<Family?>(null)

        database.getReference(DBPath.WIMK.path).child(DBPath.Family.path)
            .orderByChild("users/${auth.currentUserId}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.value != null) {
                        dataSnapshotLoop@ for (dataSnapshot in snapshot.children) {
                            val family = dataSnapshot.getValue(Family::class.java)
                            Timber.tag(logTag).d("checkFamilyRoom - onDataChange : ${dataSnapshot.key} ${dataSnapshot.value}")
                            Timber.tag(logTag).d("checkFamilyRoom - onRealDataChange : $family")

                            if (family != null) {
                                for (user in family.users) {
                                    if (user.key == auth.currentUserId) {
                                        val map = mutableMapOf<String, User>()
                                        map[user.key] = user.value
                                        result.value = Family(familyUid = dataSnapshot.key, users = map)
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

        return result.asStateFlow()
    }

    override suspend fun getCurrentFamilyWithAuthKey(key: String): StateFlow<Family?> {
        val result = MutableStateFlow<Family?>(null)
        database.getReference(DBPath.WIMK.path)
            .child(DBPath.Family.path)
            .orderByChild(DBPath.Auth.path).equalTo(key)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        Timber.tag(logTag).d("getCurrentFamilyWithAuthKey - onDataChange : ${dataSnapshot.key}, ${dataSnapshot.value}")
                        result.value = Family(familyUid = dataSnapshot.key)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        return result.asStateFlow()
    }

    override suspend fun updateAuthKey(uid: String, key: String) {
        if (uid.isNotEmpty()) {
            Timber.tag(logTag).d("updateAuthKey $key, $uid")
            database.getReference(DBPath.WIMK.path)
                .child("${DBPath.Family.path}/$uid")
                .child(DBPath.Auth.path)
                .setValue(key).await()
        }
    }

    override suspend fun onKidRegister(familyUid: String): StateFlow<Boolean?> {
        val result = MutableStateFlow<Boolean?>(null)
        Timber.tag(logTag).d("onKidRegister $familyUid")

        database.getReference(DBPath.WIMK.path)
            .child("${DBPath.Family.path}/$familyUid")
            .child(DBPath.Users.path)
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    Timber.tag(logTag).d("onChildAdded: ${snapshot.value}")
                    // 자녀 추가...
                    snapshot.getValue(User::class.java)?.let { user ->
                        if (user.relation == RelationType.Son.relation || user.relation == RelationType.Daughter.relation) {
                            result.value = true
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })

        return result.asStateFlow()
    }
}