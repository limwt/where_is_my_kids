package com.jeff.lim.wimk.screen.register

import androidx.compose.runtime.mutableStateOf
import com.jeff.lim.wimk.model.service.DatabaseService
import com.jeff.lim.wimk.model.service.LogService
import com.jeff.lim.wimk.screen.WimkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    logService: LogService
) : WimkViewModel(logService) {
    var uiState = mutableStateOf(RegisterUiState())
    private val relation
        get() = uiState.value.relation
    private val name
        get() = uiState.value.name
    private val phoneNumber
        get() = uiState.value.phoneNumber
    private val authKey
        get() = uiState.value.authKey

    fun onNameChange(newValue: String) {
        uiState.value = uiState.value.copy(name = newValue)
    }

    fun onPhoneNumberChange(newValue: String) {
        uiState.value = uiState.value.copy(phoneNumber = newValue)
    }

    fun onAuthKeyChange(newValue: String) {
        uiState.value = uiState.value.copy(authKey = newValue)
    }

    fun onRelationChange(newValue: String) {
        uiState.value = uiState.value.copy(relation = newValue)
    }

    fun onRegisterClick(openAndPopUp: (String, String) -> Unit) {

    }
}