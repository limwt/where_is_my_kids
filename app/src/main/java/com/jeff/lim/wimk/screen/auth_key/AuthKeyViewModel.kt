package com.jeff.lim.wimk.screen.auth_key

import androidx.compose.runtime.mutableStateOf
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

    fun onEvent() {
        launchCatching {
            databaseService.onKidRegister().collect { result ->
                if (result != null && result) {
                    uiState.value = uiState.value.copy(kidAdded = true)
                }
            }
        }
    }

    fun onCompleteClick(familyUid: String, openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            databaseService.updateAuthKey(familyUid, authKey)
        }
    }
}