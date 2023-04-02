package com.jeff.lim.wimk.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jeff.lim.wimk.manager.ScreenManager
import com.jeff.lim.wimk.viewmodel.FirebaseTokenViewModel

@Composable
fun InitScreen(navController: NavController, screenManager: ScreenManager, firebaseTokenViewModel: FirebaseTokenViewModel) {
    if (screenManager.availableNetwork) {
        RegisterScreen(navController, firebaseTokenViewModel)
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
    InitScreen(navController = navController, screenManager = screenManager, firebaseTokenViewModel = FirebaseTokenViewModel())
    //RegisterMainView(FirebaseTokenViewModel())
}
