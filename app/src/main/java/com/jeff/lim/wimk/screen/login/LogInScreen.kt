package com.jeff.lim.wimk.screen.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeff.lim.wimk.common.composable.BasicButton
import com.jeff.lim.wimk.common.composable.BasicToolbar
import com.jeff.lim.wimk.common.composable.EmailField
import com.jeff.lim.wimk.common.composable.PasswordField
import com.jeff.lim.wimk.common.ext.basicButton
import com.jeff.lim.wimk.common.ext.fieldModifier
import com.jeff.lim.wimk.R.string as AppText

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LogInScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LogInViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val fieldModifier = modifier.fieldModifier()
    val keyboardController = LocalSoftwareKeyboardController.current

    LogInScreenView(
        modifier = fieldModifier,
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogInClick = {
            keyboardController?.hide()
            viewModel.onLogInClick(openAndPopUp)
        }
    )
}

@Composable
private fun LogInScreenView(
    modifier: Modifier,
    uiState: LogInUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogInClick: () -> Unit
) {
    BasicToolbar(title = AppText.login)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        EmailField(uiState.email, onEmailChange, modifier)
        PasswordField(uiState.password, onPasswordChange, modifier)

        BasicButton(
            text = AppText.login,
            modifier = Modifier.basicButton(),
            action = onLogInClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun UserLogInScreenPreview() {
    LogInScreenView(
        modifier = Modifier,
        uiState = LogInUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onLogInClick = {}
    )
}
