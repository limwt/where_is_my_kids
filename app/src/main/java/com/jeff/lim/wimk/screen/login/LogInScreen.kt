package com.jeff.lim.wimk.screen.login

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LogInScreen(
    openAndPopUp: (String, String) -> Unit,
    modifier: Modifier = Modifier
    //viewModel: LogInViewModel = hiltViewModel()
) {
    /*Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val scope = rememberCoroutineScope()

        var emailText by remember { mutableStateOf("") }
        var passwordText by remember { mutableStateOf("") }

        OutlinedTextField(
            value = emailText,
            onValueChange = { emailText = it },
            enabled = true,
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            label = { Text(text = stringResource(id = R.string.text_email_hint)) }
        )

        OutlinedTextField(
            value = passwordText,
            onValueChange = { passwordText = it },
            enabled = true,
            textStyle = TextStyle(fontSize = 20.sp, color = Color.Black),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, top = 20.dp),
            label = { Text(text = stringResource(id = R.string.text_password)) },
            visualTransformation = PasswordVisualTransformation()
        )


        Button(
            onClick = {
                scope.launch {
                    logInViewModel.logIn(emailText, passwordText).collect { result ->
                        result?.let { ret ->
                            if (ret) {
                                usersViewModel.checkFamilyRoom().collect { result ->
                                    result?.let { ret ->
                                        if (ret.familyUID == null) {
                                            navController.navigate(WimkRoutes.RegisterScreen.name) {
                                                popUpTo(WimkRoutes.UserLogInScreen.name) {
                                                    inclusive = true
                                                }
                                            }
                                        } else {
                                            when (ret.users[Firebase.auth.uid]?.role) {
                                                RoleType.Dad.role,
                                                RoleType.Mom.role -> {
                                                    navController.navigate(WimkRoutes.ParentScreen.name) {
                                                        popUpTo(WimkRoutes.UserLogInScreen.name) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                                RoleType.Son.role,
                                                RoleType.Daughter.role -> {
                                                    navController.navigate(WimkRoutes.KidScreen.name) {
                                                        popUpTo(WimkRoutes.UserLogInScreen.name) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                                else -> {
                                                    navController.navigate(WimkRoutes.RegisterScreen.name) {
                                                        popUpTo(WimkRoutes.UserLogInScreen.name) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(3.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
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
                scope.launch {
                    logInViewModel.signUp(emailText, passwordText).collect { result ->
                        result?.let { ret ->
                            if (ret) {
                                usersViewModel.checkFamilyRoom().collect { result ->
                                    if (result == null) {
                                        navController.navigate(WimkRoutes.RegisterScreen.name) {
                                            popUpTo(WimkRoutes.UserLogInScreen.name) {
                                                inclusive = true
                                            }
                                        }
                                    } else {
                                        if (result.familyUID == null) {
                                            navController.navigate(WimkRoutes.RegisterScreen.name) {
                                                popUpTo(WimkRoutes.UserLogInScreen.name) {
                                                    inclusive = true
                                                }
                                            }
                                        } else {
                                            when (result.users[Firebase.auth.uid]?.role) {
                                                RoleType.Dad.role,
                                                RoleType.Mom.role -> {
                                                    navController.navigate(WimkRoutes.ParentScreen.name) {
                                                        popUpTo(WimkRoutes.UserLogInScreen.name) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                                RoleType.Son.role,
                                                RoleType.Daughter.role -> {
                                                    navController.navigate(WimkRoutes.KidScreen.name) {
                                                        popUpTo(WimkRoutes.UserLogInScreen.name) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                                else -> {
                                                    navController.navigate(WimkRoutes.RegisterScreen.name) {
                                                        popUpTo(WimkRoutes.UserLogInScreen.name) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            },
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(3.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 20.dp, end = 20.dp),
            enabled = emailText.isNotEmpty() && passwordText.isNotEmpty()
        ) {
            Text(
                text = stringResource(id = R.string.button_text_signup),
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }*/
}

@Preview(showBackground = true)
@Composable
fun UserLogInScreenPreview() {
    /*val navController = rememberNavController()
    LogInScreen(navController = navController)*/
}
