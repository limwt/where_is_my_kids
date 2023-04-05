package com.jeff.lim.wimk.screen.init

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeff.lim.wimk.common.composable.BasicToolbar
import com.jeff.lim.wimk.common.composable.RegularCardEditor
import com.jeff.lim.wimk.common.ext.card
import com.jeff.lim.wimk.common.ext.spacer
import com.jeff.lim.wimk.R.string as AppText


@Composable
fun InitScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InitViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState
    InitScreenView(
        modifier = modifier,
        uiState = uiState,
        onLoginClick = { viewModel.onLoginClick(openAndPopUp) },
        onSignUpClick = { viewModel.onSignUpClick(openAndPopUp) }
    )
}

@Composable
fun InitScreenView(
    modifier: Modifier,
    uiState: InitUiState,
    onLoginClick: (() -> Unit),
    onSignUpClick: (() -> Unit)
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicToolbar(title = AppText.init)

        Spacer(modifier = Modifier.spacer())

        if (uiState.isAnonymousAccount) {
            RegularCardEditor(
                title = AppText.login,
                icon = -1,
                content = "",
                modifier = Modifier.card(),
                onEditClick = onLoginClick
            )

            RegularCardEditor(
                title = AppText.create_account,
                icon = -1,
                content = "",
                modifier = Modifier.card(),
                onEditClick = onSignUpClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun InitScreenPreview() {
    InitScreenView(modifier = Modifier, uiState = InitUiState(), onLoginClick = {}, onSignUpClick = {})
}