package com.jeff.lim.wimk.screen.init

import com.jeff.lim.wimk.WimkRoutes
import com.jeff.lim.wimk.model.service.LogService
import com.jeff.lim.wimk.screen.WimkViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InitViewModel @Inject constructor(
    logService: LogService/*,
    private val accountService: AccountService,
    private val storageService: StorageService*/
) : WimkViewModel(logService) {
    val uiState = InitUiState(isAnonymousAccount = true)

    fun onLoginClick(openAndPopUp: (String, String) -> Unit) = openAndPopUp(WimkRoutes.LogInScreen.name, WimkRoutes.InitScreen.name)

    fun onSignUpClick(openAndPopUp: (String, String) -> Unit) = openAndPopUp(WimkRoutes.SignUpScreen.name, WimkRoutes.InitScreen.name)
}