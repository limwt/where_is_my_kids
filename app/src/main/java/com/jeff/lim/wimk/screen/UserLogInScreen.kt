package com.jeff.lim.wimk.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jeff.lim.wimk.R
import com.jeff.lim.wimk.ui.theme.WIMKTheme
import com.jeff.lim.wimk.viewmodel.UsersViewModel

@Composable
fun UserLogInScreen(navController: NavController, userViewModel: UsersViewModel) {
    WIMKTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var nameText by remember { mutableStateOf("") }
            var emailText by remember { mutableStateOf("") }
            var passwordText by remember { mutableStateOf("") }
            val authResult by userViewModel.authResult.observeAsState(initial = false)

            if (authResult) {
                navController.navigate(ScreenType.InitScreen.name) {
                    popUpTo(ScreenType.UserLogInScreen.name) {
                        inclusive = true
                    }
                }
            }

            OutlinedTextField(
                value = emailText,
                onValueChange = { emailText = it },
                enabled = true,
                textStyle = TextStyle(fontSize = 30.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 50.dp),
                label = { Text(text = stringResource(id = R.string.text_email_hint)) }
            )

            OutlinedTextField(
                value = nameText,
                onValueChange = { nameText = it },
                enabled = true,
                textStyle = TextStyle(fontSize = 30.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 50.dp, top = 20.dp),
                label = { Text(text = stringResource(id = R.string.text_name)) }
            )

            OutlinedTextField(
                value = passwordText,
                onValueChange = { passwordText = it },
                enabled = true,
                textStyle = TextStyle(fontSize = 30.sp, color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 50.dp, end = 50.dp, top = 20.dp),
                label = { Text(text = stringResource(id = R.string.text_password)) },
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {
                    userViewModel.logIn(nameText, emailText, passwordText)
                },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(3.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 50.dp, end = 50.dp),
                enabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.button_text_login),
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

            Button(
                onClick = {
                    userViewModel.signUp(nameText, emailText, passwordText)
                },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(3.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp, start = 50.dp, end = 50.dp),
                enabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
            ) {
                Text(
                    text = stringResource(id = R.string.button_text_signup),
                    fontSize = 20.sp,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserLogInScreenPreview() {
    val navController = rememberNavController()
    UserLogInScreen(navController = navController, userViewModel = UsersViewModel())
}
