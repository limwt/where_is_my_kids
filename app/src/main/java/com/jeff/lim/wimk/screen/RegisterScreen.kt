package com.jeff.lim.wimk.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jeff.lim.wimk.manager.ScreenManager

@Composable
fun RegisterScreen(navController: NavController, screenManager: ScreenManager) {
    if (screenManager.availableNetwork) {
        NoRegisterScreen(navController)
    } else {
        NoNetworkScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    val navController = rememberNavController()
    val screenManager = ScreenManager(LocalContext.current)
    screenManager.availableNetwork = true
    RegisterScreen(navController = navController, screenManager = screenManager)
    //RegisterMainView(FirebaseTokenViewModel())
}
