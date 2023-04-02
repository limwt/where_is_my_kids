package com.jeff.lim.wimk.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jeff.lim.wimk.manager.ScreenManager
import com.jeff.lim.wimk.viewmodel.WimkViewModel

@Composable
fun InitScreen(navController: NavController, screenManager: ScreenManager, wimkViewModel: WimkViewModel) {
    if (screenManager.availableNetwork) {
        RegisterScreen(navController, wimkViewModel)
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
    InitScreen(navController = navController, screenManager = screenManager, wimkViewModel = WimkViewModel())
    //RegisterMainView(FirebaseTokenViewModel())
}
