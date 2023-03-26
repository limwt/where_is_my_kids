package com.jeff.lim.wimk.manager

import android.content.Context
import android.net.ConnectivityManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScreenManager @Inject constructor(@ApplicationContext context: Context) {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val connectivityManager = getSystemService(ConnectivityManager::class.java)

    fun showRegisterView() {

    }


}