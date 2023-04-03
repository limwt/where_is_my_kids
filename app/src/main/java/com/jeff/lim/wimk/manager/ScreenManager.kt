package com.jeff.lim.wimk.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.jeff.lim.wimk.database.DBPath
import com.jeff.lim.wimk.database.RoleType
import com.jeff.lim.wimk.database.UserModel
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenManager @Inject constructor(@ApplicationContext context: Context) {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    private val auth = Firebase.auth
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    var availableNetwork = false

    init {
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network : Network) {
                Timber.tag(logTag).d("The default network is now: $network")
                availableNetwork = true
            }

            override fun onLost(network : Network) {
                Timber.tag(logTag).d("The application no longer has a default network. The last default network was $network")
                availableNetwork = false
            }

            override fun onCapabilitiesChanged(network : Network, networkCapabilities : NetworkCapabilities) {
                //Timber.tag(logTag).d("The default network changed capabilities: $networkCapabilities")
            }

            override fun onLinkPropertiesChanged(network : Network, linkProperties : LinkProperties) {
                //Timber.tag(logTag).d("The default network changed link properties: $linkProperties")
            }
        })
    }

    fun checkRole(onResult: (String) -> Unit) {
        firebaseDatabase.getReference(DBPath.WIMK.path)
            .child(DBPath.Users.path)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dataSnapshot in snapshot.children) {
                        val userModel = dataSnapshot.getValue(UserModel::class.java)
                        Timber.tag(logTag).d("checkRole : $userModel")

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