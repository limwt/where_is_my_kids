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
import com.jeff.lim.wimk.common.composable.BasicButton
import com.jeff.lim.wimk.common.composable.BasicField
import com.jeff.lim.wimk.common.composable.BasicToolbar
import com.jeff.lim.wimk.common.composable.CardSelector
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
        BasicField(
            text = AppText.name,
            value = uiState.name,
            onNewValue = onNameChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
        )

        BasicField(
            text = AppText.phone_number,
            value = uiState.phoneNumber,
            onNewValue = onPhoneNumberChange,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp)
        )

        if (uiState.relation == RelationType.Son.relation || uiState.relation == RelationType.Daughter.relation) {
            BasicField(
                text = AppText.auth_key,
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










/*WIMKTheme {
       Column(
           modifier = Modifier
               .fillMaxSize()
               .padding(start = 20.dp, end = 20.dp),
           horizontalAlignment = Alignment.CenterHorizontally,
           verticalArrangement = Arrangement.Center
       ) {
           var dialogState by remember { mutableStateOf(false) }
           var relationIndex by remember { mutableStateOf(-1) }
           var nameText by remember { mutableStateOf("") }
           var authKeyText by remember { mutableStateOf("") }
           val relationList = listOf(RelationType.Mom, RelationType.Dad, RelationType.Son, RelationType.Daughter, RelationType.Other)
           val scope = rememberCoroutineScope()

           if (dialogState) {
               SelectRelationDialog(
                   defaultSelected = relationIndex,
                   onDismissRequest = {
                       relationIndex = it
                       dialogState = false
                   }
               )
           }

           OutlinedTextField(
               value = nameText,
               onValueChange = { nameText = it },
               enabled = true,
               textStyle = TextStyle(fontSize = 30.sp, color = Color.Black),
               modifier = Modifier.fillMaxWidth(),
               label = { Text(text = stringResource(id = R.string.text_name)) }
           )

           if (relationIndex == RelationType.Son.ordinal || relationIndex == RelationType.Dad.ordinal) {
               OutlinedTextField(
                   value = authKeyText,
                   onValueChange = { authKeyText = it },
                   enabled = true,
                   textStyle = TextStyle(fontSize = 30.sp, color = Color.Black),
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(top = 50.dp),
                   label = { Text(text = stringResource(id = R.string.text_auth_key)) },
               )
           }

           Row(modifier = Modifier
               .fillMaxWidth()
               .padding(top = 50.dp)) {
               Text(
                   text = stringResource(id = R.string.text_register_relation),
                   fontSize = 25.sp,
                   modifier = Modifier.align(Alignment.CenterVertically)
               )

               Button(
                   onClick = { dialogState = !dialogState },
                   colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(start = 20.dp)
               ) {
                   Text(
                       text = if (relationIndex == -1) stringResource(id = R.string.text_no_relation) else relationList[relationIndex].relation,
                       fontSize = 20.sp
                   )
               }
           }

           Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(top = 20.dp)
           ) {
               Button(
                   onClick = {
                       scope.launch {
                           when (relationList[relationIndex]) {
                               RelationType.Son,
                               RelationType.Daughter -> {
                                   userViewModel.getFamilyUID(authKeyText).collect { result ->
                                       if (!result.isNullOrEmpty()) {
                                           userViewModel.updateUser(result, nameText, relationList[relationIndex].relation).collect { result ->
                                               result?.let { ret ->
                                                   if (ret) {
                                                       navController.navigate(WimkRoutes.KidScreen.name) {
                                                           popUpTo(WimkRoutes.RegisterScreen.name) {
                                                               inclusive = true
                                                           }
                                                       }
                                                   }
                                               }
                                           }
                                       }
                                   }
                               }
                               RelationType.Dad,
                               RelationType.Mom -> {
                                   userViewModel.updateUser("", nameText, relationList[relationIndex].relation).collect { result ->
                                       result?.let { ret ->
                                           if (ret) {
                                               navController.navigate(WimkRoutes.AuthKeyScreen.name) {
                                                   popUpTo(WimkRoutes.RegisterScreen.name) {
                                                       inclusive = true
                                                   }
                                               }
                                           }
                                       }
                                   }
                               }
                               else -> {}
                           }
                       }
                   },
                   shape = RoundedCornerShape(20.dp),
                   border = BorderStroke(3.dp, Color.Black),
                   colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
                   modifier = Modifier
                       .width(150.dp)
                       .weight(1.0f)
                       .padding(end = 5.dp),
                   enabled = relationIndex >= 0
               ) {
                   Text(
                       text = stringResource(id = R.string.button_save_text),
                       fontSize = 20.sp,
                       modifier = Modifier.padding(horizontal = 30.dp, vertical = 6.dp)
                   )
               }
           }
       }
   }*/

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