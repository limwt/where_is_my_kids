package com.jeff.lim.wimk.model.service.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.jeff.lim.wimk.database.FamilyModel
import com.jeff.lim.wimk.database.RoleType
import com.jeff.lim.wimk.model.Family
import com.jeff.lim.wimk.model.service.AccountService
import com.jeff.lim.wimk.model.service.DatabaseService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import javax.inject.Inject

class DatabaseServiceImpl @Inject constructor(
    private val auth: AccountService,
    private val database: FirebaseDatabase
) : DatabaseService {
    private val logTag = "[WIMK]DatabaseService"

    override val currentFamily: Flow<Family>
        get() = callbackFlow {
            var uid = ""
            var role = RoleType.Init.role
            database.getReference(DatabaseService.Path.WIMK.path)
                .child(DatabaseService.Path.Family.path)
                .orderByChild("users/${auth.currentUserId}")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.value != null) {
                            dataSnapshotLoop@ for (dataSnapshot in snapshot.children) {
                                Timber.tag(logTag).d("current family ${dataSnapshot.value}")
                                val family = dataSnapshot.getValue(FamilyModel::class.java)

                                if (family != null) {
                                    for (u in family.users) {
                                        if (u.key == auth.currentUserId) {
                                            uid = dataSnapshot.key ?: ""
                                            role = u.value.role
                                            break@dataSnapshotLoop
                                        }
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })

            this.trySend(Family(uid, role))
        }
}