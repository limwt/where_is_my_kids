package com.jeff.lim.wimk.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
import com.jeff.lim.wimk.database.RoleType
import com.jeff.lim.wimk.di.FirebaseDbRepository
import com.jeff.lim.wimk.ui.theme.WIMKTheme
import com.jeff.lim.wimk.viewmodel.UsersViewModel

@Composable
fun UserLogInScreen(navController: NavController, userViewModel: UsersViewModel) {
    WIMKTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
                    userViewModel.logIn(emailText, passwordText) { result ->
                        if (result) {
                            userViewModel.checkFamilyRoom { userData ->
                                if (userData.first == null) {
                                    navController.navigate(ScreenType.RegisterScreen.name) {
                                        popUpTo(ScreenType.UserLogInScreen.name) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    when (userData.second!!) {
                                        RoleType.Dad.role,
                                        RoleType.Mom.role -> {
                                            navController.navigate(ScreenType.ParentScreen.name) {
                                                popUpTo(ScreenType.UserLogInScreen.name) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        RoleType.Init.role -> {
                                            navController.navigate(ScreenType.RegisterScreen.name) {
                                                popUpTo(ScreenType.UserLogInScreen.name) {
                                                    inclusive = true
                                                }
                                            }

                                        }
                                        else -> {
                                            navController.navigate(ScreenType.KidScreen.name) {
                                                popUpTo(ScreenType.UserLogInScreen.name) {
                                                    inclusive = true
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
                    userViewModel.signUp(emailText, passwordText) { result ->
                        if (result) {
                            userViewModel.checkFamilyRoom { userData ->
                                if (userData.first == null) {
                                    navController.navigate(ScreenType.RegisterScreen.name) {
                                        popUpTo(ScreenType.UserLogInScreen.name) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    when (userData.second!!) {
                                        RoleType.Dad.role,
                                        RoleType.Mom.role -> {
                                            navController.navigate(ScreenType.ParentScreen.name) {
                                                popUpTo(ScreenType.UserLogInScreen.name) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        RoleType.Son.role,
                                        RoleType.Daughter.role -> {
                                            navController.navigate(ScreenType.KidScreen.name) {
                                                popUpTo(ScreenType.UserLogInScreen.name) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                        else -> {
                                            navController.navigate(ScreenType.RegisterScreen.name) {
                                                popUpTo(ScreenType.UserLogInScreen.name) {
                                                    inclusive = true
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
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UserLogInScreenPreview() {
    val navController = rememberNavController()
    UserLogInScreen(navController = navController, userViewModel = UsersViewModel(FirebaseDbRepository()))
}
