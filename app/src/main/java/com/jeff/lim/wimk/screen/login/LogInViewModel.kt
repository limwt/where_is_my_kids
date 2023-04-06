package com.jeff.lim.wimk.screen.login

import androidx.compose.runtime.mutableStateOf
import com.jeff.lim.wimk.R
import com.jeff.lim.wimk.common.snack_bar.SnackBarManager
import com.jeff.lim.wimk.model.service.AccountService
import com.jeff.lim.wimk.model.service.DatabaseService
import com.jeff.lim.wimk.model.service.LogService
import com.jeff.lim.wimk.screen.WimkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val accountService: AccountService,
    private val databaseService: DatabaseService,
    logService: LogService
) : WimkViewModel(logService) {
    private val logTag = "[WIMK]${this::class.java.simpleName}"

    var uiState = mutableStateOf(LogInUiState())
    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onLogInClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            if (accountService.logIn(email, password)) {
                databaseService.currentFamily.collect { family ->
                    Timber.tag(logTag).d("onLoginClick - currentFamily $family")
                    if (family.uid.isEmpty()) {

                    } else {
                        when (family.relation) {

                        }
                    }
                }
            } else {
                SnackBarManager.showMessage(R.string.login_error)
            }
        }
    }
}