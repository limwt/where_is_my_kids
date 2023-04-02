package com.jeff.lim.wimk

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jeff.lim.wimk.manager.FirebaseTokenManager
import com.jeff.lim.wimk.manager.ScreenManager
import com.jeff.lim.wimk.screen.InitScreen
import com.jeff.lim.wimk.screen.RegisterScreen
import com.jeff.lim.wimk.screen.ScreenType
import com.jeff.lim.wimk.screen.UserLogInScreen
import com.jeff.lim.wimk.ui.theme.WIMKTheme
import com.jeff.lim.wimk.viewmodel.FirebaseTokenViewModel
import com.jeff.lim.wimk.viewmodel.UsersViewModel
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val fireBaseTokenViewModel: FirebaseTokenViewModel by viewModels()
    private val usersViewModel: UsersViewModel by viewModels()

    @Inject lateinit var firebaseTokenManager: FirebaseTokenManager
    @Inject lateinit var screenManager: ScreenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //Generate android unique ID
            fireBaseTokenViewModel.updateAndroidId(getAndroidId())
            ShowScreen(screenManager, fireBaseTokenViewModel, usersViewModel)
        }
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
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
fun ShowScreen(screenManager: ScreenManager, firebaseTokenViewModel: FirebaseTokenViewModel, usersViewModel: UsersViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = ScreenType.UserLogInScreen.name
    ) {
        composable(ScreenType.UserLogInScreen.name) {
            UserLogInScreen(navController = navController, userViewModel = usersViewModel)
        }
        composable(ScreenType.InitScreen.name) {
            //Text(text = ScreenType.RegisterScreen.name)
            InitScreen(navController = navController, screenManager = screenManager, firebaseTokenViewModel = firebaseTokenViewModel)
        }
        composable(ScreenType.NewRegisterScreen.name) {
            RegisterScreen(navController = navController, firebaseTokenViewModel = firebaseTokenViewModel)
        }

        // TODO: 다른 두개의 화면을 추가하자
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
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    //RegisterMainView(FirebaseTokenViewModel())
    val screenManager = ScreenManager(LocalContext.current)
    screenManager.availableNetwork = true
    ShowScreen(screenManager, FirebaseTokenViewModel(), UsersViewModel())
}