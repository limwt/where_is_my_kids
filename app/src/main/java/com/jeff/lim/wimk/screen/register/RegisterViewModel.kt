package com.jeff.lim.wimk.screen.register

import androidx.compose.runtime.mutableStateOf
import com.jeff.lim.wimk.WimkRoutes
import com.jeff.lim.wimk.common.snack_bar.SnackBarManager
import com.jeff.lim.wimk.database.RelationType
import com.jeff.lim.wimk.model.service.DatabaseService
import com.jeff.lim.wimk.model.service.LogService
import com.jeff.lim.wimk.screen.WimkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.jeff.lim.wimk.R.string as AppText

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
        launchCatching {
            when (relation) {
                RelationType.Dad.relation,
                RelationType.Mom.relation -> {
                    databaseService.register(name, phoneNumber, relation)
                    openAndPopUp(WimkRoutes.AuthKeyScreen.name, WimkRoutes.RegisterScreen.name)
                }
                RelationType.Son.relation,
                RelationType.Dad.relation -> {

                }
                else -> {
                    SnackBarManager.showMessage(AppText.register_error)
                }
            }
        }
    }
}