package com.jeff.lim.wimk.screen.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeff.lim.wimk.common.composable.*
import com.jeff.lim.wimk.common.ext.basicButton
import com.jeff.lim.wimk.common.ext.fieldModifier
import com.jeff.lim.wimk.R.string as AppText

@Composable
fun SignUpScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    val fieldModifier = modifier.fieldModifier()

    SignUpScreenView(
        modifier = fieldModifier,
        uiState = uiState,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onRepeatPasswordChange = viewModel::onRepeatPasswordChange,
        onSignUpClick = { viewModel.onSignUpClick(openAndPopUp) }
    )
}

@Composable
fun SignUpScreenView(
    modifier: Modifier,
    uiState: SignUpUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRepeatPasswordChange: (String) -> Unit,
    onSignUpClick: () -> Unit
) {
    BasicToolbar(AppText.create_account)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailField(uiState.email, onEmailChange, modifier)
        PasswordField(uiState.password, onPasswordChange, modifier)
        RepeatPasswordField(uiState.repeatPassword, onRepeatPasswordChange, modifier)

        BasicButton(
            text = AppText.create_account,
            modifier = Modifier.basicButton(),
            action = onSignUpClick
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreenView(
        modifier = Modifier,
        uiState = SignUpUiState(),
        onEmailChange = {},
        onPasswordChange = {},
        onRepeatPasswordChange = {},
        onSignUpClick = {}
    )
}
