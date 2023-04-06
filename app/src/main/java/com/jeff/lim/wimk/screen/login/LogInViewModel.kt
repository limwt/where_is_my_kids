package com.jeff.lim.wimk.screen.login

import androidx.compose.runtime.mutableStateOf
import com.jeff.lim.wimk.R
import com.jeff.lim.wimk.WimkRoutes
import com.jeff.lim.wimk.common.snack_bar.SnackBarManager
import com.jeff.lim.wimk.model.service.AccountService
import com.jeff.lim.wimk.model.service.LogService
import com.jeff.lim.wimk.screen.WimkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogInViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : WimkViewModel(logService) {
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

    fun onLoginClick(openAndPopUp: (String, String) -> Unit) {
        launchCatching {
            accountService.logIn(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        openAndPopUp(WimkRoutes.SignUpScreen.name, WimkRoutes.LogInScreen.name)
                    } else {
                        SnackBarManager.showMessage(R.string.login_error)
                    }
                }
                .addOnFailureListener {
                    SnackBarManager.showMessage(R.string.login_error)
                }
        }
    }
}