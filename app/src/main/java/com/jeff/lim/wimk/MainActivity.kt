package com.jeff.lim.wimk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jeff.lim.wimk.manager.FirebaseTokenManager
import com.jeff.lim.wimk.manager.ScreenManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val logTag = "[WIMK]${this::class.java.simpleName}"

    @Inject lateinit var firebaseTokenManager: FirebaseTokenManager
    @Inject lateinit var screenManager: ScreenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.tag(logTag).d("onCreate")
        setContent {
            WimkApp()
        }
    }
}

/*@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun WIMKApp(content: @Composable () -> Unit) {
    WIMKTheme {
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        
        Scaffold(topBar = {
            TopAppBar(
                backgroundColor = Color.Gray,
                elevation = 5.dp,
                title = { Text(text = "TEST") },
                navigationIcon = {

                },
                actions = {

                }
            )
        }) {
            content()
        }
    }
}

@Composable
fun ShowScreen(screenManager: ScreenManager) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = WimkRoutes.UserLogInScreen.name
    ) {
        composable(WimkRoutes.UserLogInScreen.name) {
            val logInViewModel = hiltViewModel<LogInViewModel>()
            val usersViewModel = hiltViewModel<UsersViewModel>()
            LogInScreen(navController = navController, logInViewModel = logInViewModel, usersViewModel = usersViewModel)
        }
        composable(WimkRoutes.RegisterScreen.name) {
            val usersViewModel = hiltViewModel<UsersViewModel>()
            RegisterScreen(navController = navController, userViewModel = usersViewModel)
        }
        composable(WimkRoutes.ParentScreen.name) {
            ParentScreen()
        }
        composable(WimkRoutes.KidScreen.name) {
            KidScreen()
        }
        composable(WimkRoutes.AuthKeyScreen.name) {
            val usersViewModel = hiltViewModel<UsersViewModel>()
            AuthKeyScreen(usersViewModel = usersViewModel)
        }
    }
}





@Composable
fun FavoritePositionView() {
    WIMKTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            FloatingButton()
            MapView()
        }
    }
}

@Composable
fun FloatingButton() {
    Box(modifier = Modifier.fillMaxSize()) {
        val contextForToast = LocalContext.current.applicationContext

        FloatingActionButton(
            modifier = Modifier
                .padding(all = 16.dp)
                .align(alignment = Alignment.BottomEnd),
            onClick = {
                Toast.makeText(contextForToast, "Click", Toast.LENGTH_SHORT).show()
            }) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = "Add")
        }
    }
}

@OptIn(ExperimentalNaverMapApi::class)
@Composable
fun MapView() {
    NaverMap(modifier = Modifier.fillMaxSize())
}*/

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WimkApp()
}