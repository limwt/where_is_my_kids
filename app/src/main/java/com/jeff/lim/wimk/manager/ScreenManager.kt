package com.jeff.lim.wimk.manager

import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenManager @Inject constructor(@ApplicationContext context: Context) {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val connectivityManager = context.getSystemService(ConnectivityManager::class.java)

    var availableNetwork = false
    var isSignedUp = Firebase.auth.currentUser

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
                Timber.tag(logTag).d("The default network changed capabilities: $networkCapabilities")
            }

            override fun onLinkPropertiesChanged(network : Network, linkProperties : LinkProperties) {
                Timber.tag(logTag).d("The default network changed link properties: $linkProperties")
            }
        })
    }
}