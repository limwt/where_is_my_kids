package com.jeff.lim.wimk

import android.content.res.Resources
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.jeff.lim.wimk.common.snack_bar.SnackBarManager
import com.jeff.lim.wimk.screen.auth_key.AuthKeyScreen
import com.jeff.lim.wimk.screen.init.InitScreen
import com.jeff.lim.wimk.screen.login.LogInScreen
import com.jeff.lim.wimk.screen.parent.ParentScreen
import com.jeff.lim.wimk.screen.register.RegisterScreen
import com.jeff.lim.wimk.screen.sign_up.SignUpScreen
import com.jeff.lim.wimk.ui.theme.WIMKTheme
import kotlinx.coroutines.CoroutineScope

@Composable
fun WimkApp() {
    WIMKTheme {
        Surface(color = MaterialTheme.colors.background) {
            val appState = rememberAppState()

            Scaffold(
                snackbarHost = {
                    SnackbarHost(
                        hostState = it,
                        modifier = Modifier.padding(8.dp),
                        snackbar = { snackBarData ->
                            Snackbar(snackBarData, contentColor = MaterialTheme.colors.onPrimary)
                        }
                    )
                },
                scaffoldState = appState.scaffoldState
            ) { innerPaddingModifier ->
                NavHost(
                    navController = appState.navController,
                    startDestination = WimkRoutes.InitScreen.name,
                    modifier = Modifier.padding(innerPaddingModifier)
                ) {
                    wimkGraph(appState)
                }
            }
        }
    }
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Composable
fun rememberAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    navController: NavHostController = rememberNavController(),
    snackBarManager: SnackBarManager = SnackBarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(scaffoldState, navController, snackBarManager, resources, coroutineScope) {
        WimkAppState(scaffoldState, navController, snackBarManager, resources, coroutineScope)
    }

fun NavGraphBuilder.wimkGraph(appState: WimkAppState) {
    composable(WimkRoutes.InitScreen.name) {
        InitScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(WimkRoutes.SignUpScreen.name) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(WimkRoutes.LogInScreen.name) {
        LogInScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(WimkRoutes.RegisterScreen.name) {
        RegisterScreen(openAndPopUp = { route, popUp, arg -> appState.navigateAndPopUp(route, popUp, arg) })
    }

    composable(
        route = "${WimkRoutes.AuthKeyScreen.name}/{familyUid}",
        arguments = listOf(
            navArgument("familyUid") { type = NavType.StringType }
        )
    ) { navBackStackEntry ->
        // Retrieve the passed argument
        val familyUid = navBackStackEntry.arguments?.getString("familyUid")
        AuthKeyScreen(
            familyUid = familyUid ?: "",
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }

    composable(
        route = "${WimkRoutes.ParentScreen.name}/{familyUid}",
        arguments = listOf(
            navArgument("familyUid") { type = NavType.StringType }
        )
    ) { navBackStackEntry ->
        val familyUid = navBackStackEntry.arguments?.getString("familyUid")
        ParentScreen(
            familyUid = familyUid ?: "",
            openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) }
        )
    }
}