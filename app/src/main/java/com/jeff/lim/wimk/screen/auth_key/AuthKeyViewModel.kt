package com.jeff.lim.wimk.screen.auth_key

import androidx.compose.runtime.mutableStateOf
import com.jeff.lim.wimk.WimkRoutes
import com.jeff.lim.wimk.model.service.DatabaseService
import com.jeff.lim.wimk.model.service.LogService
import com.jeff.lim.wimk.screen.WimkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthKeyViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    logService: LogService
) : WimkViewModel(logService) {
    var uiState = mutableStateOf(AuthKeyUiState())

    private val authKey
        get() = uiState.value.authKey

    fun onAuthKeyChange() {
        uiState.value = uiState.value.copy(authKey = key())
    }

    fun onKidRegister(familyUid: String) {
        launchCatching {
            databaseService.onKidRegister(familyUid).collect { result ->
                if (result != null && result) {
                    uiState.value = uiState.value.copy(kidAdded = true)
                }
            }
        }
    }

    fun updateAuthKey(uid: String) {
        launchCatching {
            databaseService.updateAuthKey(uid, authKey)
        }
    }

    fun onComplete(familyUid: String, openAndPopUp: (String, String, String) -> Unit) {
        openAndPopUp(WimkRoutes.ParentScreen.name, WimkRoutes.AuthKeyScreen.name, familyUid)
    }

    private fun key(): String {
        val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..8)
            .map { charset.random() }
            .joinToString("")
    }
}