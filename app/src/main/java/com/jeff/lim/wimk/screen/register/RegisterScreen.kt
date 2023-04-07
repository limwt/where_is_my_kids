package com.jeff.lim.wimk.screen.register

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
import com.jeff.lim.wimk.common.composable.*
import com.jeff.lim.wimk.common.ext.basicButton
import com.jeff.lim.wimk.common.ext.card
import com.jeff.lim.wimk.database.RelationType
import com.jeff.lim.wimk.R.string as AppText

@Composable
fun RegisterScreen(
    openAndPopUp: (String, String, String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState

    RegisterScreenView(
        modifier = modifier,
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onPhoneNumberChange = viewModel::onPhoneNumberChange,
        onAuthKeyChange = viewModel::onAuthKeyChange,
        onRelationChange = viewModel::onRelationChange,
        onRegisterClick = { viewModel.onRegisterClick(openAndPopUp) }
    )
}

@Composable
private fun RegisterScreenView(
    modifier: Modifier,
    uiState: RegisterUiState,
    onNameChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onAuthKeyChange: (String) -> Unit,
    onRelationChange: (String) -> Unit,
    onRegisterClick: () -> Unit
) {
    BasicToolbar(title = AppText.register)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        NameField(
            value = uiState.name,
            onNewValue = onNameChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )

        PhoneNumberField(
            value = uiState.phoneNumber,
            onNewValue = onPhoneNumberChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp)
        )

        if (uiState.relation == RelationType.Son.relation || uiState.relation == RelationType.Daughter.relation) {
            AuthKeyField(
                value = uiState.authKey,
                onNewValue = onAuthKeyChange,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 16.dp, end = 16.dp)
            )
        }

        CardSelectors(uiState.relation, onRelationChange)

        BasicButton(
            text = AppText.register,
            modifier = Modifier.basicButton(),
            action = onRegisterClick
        )
    }
}

@Composable
private fun CardSelectors(
    relation: String,
    onRelationChange: (String) -> Unit
) {
    val relationSelection = RelationType.getByName(relation).relation

    CardSelector(
        AppText.relation,
        RelationType.getOptions(),
        relationSelection,
        Modifier.card()
    ) { newValue ->
        onRelationChange(newValue)
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreenView(
        modifier = Modifier,
        uiState = RegisterUiState(relation = RelationType.Son.relation),
        onNameChange = {},
        onPhoneNumberChange = {},
        onAuthKeyChange = {},
        onRelationChange = {},
        onRegisterClick = {}
    )
}