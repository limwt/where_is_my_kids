package com.jeff.lim.wimk.screen.auth_key

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jeff.lim.wimk.common.composable.AuthKeyText
import com.jeff.lim.wimk.common.composable.BasicText
import com.jeff.lim.wimk.common.composable.BasicToolbar
import com.jeff.lim.wimk.common.composable.EnableButton
import com.jeff.lim.wimk.common.ext.basicButton
import com.jeff.lim.wimk.R.string as AppText

@Composable
fun AuthKeyScreen(
    modifier: Modifier = Modifier,
    familyUid: String = "",
    openAndPopUp: (String, String) -> Unit,
    viewModel: AuthKeyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState
    viewModel.updateAuthKey(familyUid)
    viewModel.onKidRegister(familyUid)
    AuthKeyScreenView(
        modifier = modifier,
        uiState = uiState,
        onRegisterClick = { viewModel.onCompleteClick(familyUid, openAndPopUp) }
    )
}

@Composable
private fun AuthKeyScreenView(
    modifier: Modifier,
    uiState: AuthKeyUiState,
    onRegisterClick: () -> Unit
) {
    BasicToolbar(title = AppText.add_kids)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        BasicText(
            text = AppText.auth_key,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )

        AuthKeyText(
            text = uiState.authKey,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 20.dp)
        )

        BasicText(
            text = AppText.guide_add_kids,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 20.dp)
        )

        EnableButton(
            text = AppText.add_kids,
            modifier = Modifier.basicButton(),
            action = onRegisterClick,
            enabled = uiState.kidAdded
        )
    }
}







/*
Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val authKey = getRandomString()
        val updateUserState by usersViewModel.userUpdateResult.collectAsState()
        var enabledButton by remember { mutableStateOf(false) }

        updateUserState?.let { state ->
            if (state) {
                enabledButton = true
            }
        }
        usersViewModel.getUserInfo()
        usersViewModel.updateAuthKey(authKey)

        Text(
            text = authKey,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(id = R.string.text_register_child),
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = {

            },
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(3.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
            modifier = Modifier
                .width(150.dp)
                .padding(top = 100.dp, end = 5.dp),
            enabled = enabledButton
        ) {
            Text(
                text = stringResource(id = R.string.button_save_text),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 6.dp)
            )
        }
    }
 */


@Preview(showBackground = true)
@Composable
fun AuthKeyScreenPreview() {
    AuthKeyScreenView(
        modifier = Modifier,
        uiState = AuthKeyUiState(),
        onRegisterClick = {}
    )
}
