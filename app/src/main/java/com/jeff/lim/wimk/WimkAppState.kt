package com.jeff.lim.wimk

import android.content.res.Resources
import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Stable
import androidx.navigation.NavHostController
import com.jeff.lim.wimk.common.snack_bar.SnackBarManager
import com.jeff.lim.wimk.common.snack_bar.SnackBarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

@Stable
class WimkAppState(
    val scaffoldState: ScaffoldState,
    val navController: NavHostController,
    private val snackBarManager: SnackBarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackBarManager.snackBarMessages.filterNotNull().collect { snackBarMessage ->
                val text = snackBarMessage.toMessage(resources)
                scaffoldState.snackbarHostState.showSnackbar(text)
            }
        }
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String, arg: String = "") {
        val destination = if (route == WimkRoutes.AuthKeyScreen.name) {
            "$route/$arg"
        } else {
            route
        }

        navController.navigate(destination) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }

    fun navigateAndPopUpWithArguments(
        route: String,
        popUp: String,
        familyUid: String
    ) {
        // If argument is supplied, navigate using that argument
        navController.navigate("$route/$familyUid") {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }
}