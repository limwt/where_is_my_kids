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

    fun onCompleteClick(familyUid: String?, openAndPopUp: (String, String) -> Unit) {
        familyUid?.let { uid ->
            launchCatching {
                // TODO : 부모의 자녀 관리 화면으로 이동...
                //databaseService.updateAuthKey(uid, authKey)
            }
        }
    }
}