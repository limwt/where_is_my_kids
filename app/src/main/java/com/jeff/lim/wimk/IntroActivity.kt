package com.jeff.lim.wimk

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.jeff.lim.wimk.manager.FirebaseTokenManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class IntroActivity : ComponentActivity() {
    @Inject lateinit var firebaseTokenManager: FirebaseTokenManager
    private val logTag = "[WIMK]${this::class.java.simpleName}"

    private val permissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.READ_PHONE_STATE, false) -> {
                Timber.tag(logTag).d("getPermission - READ_PHONE_STATE")

                Intent(this, MainActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPermissions()
    }

    private fun checkPermissions() {
        if (verifyPermissions(this)) {
            Intent(this, MainActivity::class.java).also {
                startActivity(it)
                finish()
            }
        } else {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        permissionRequest.launch(arrayOf(Manifest.permission.READ_PHONE_STATE))
    }

    private fun verifyPermissions(context: Context): Boolean {
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        return true
    }
}