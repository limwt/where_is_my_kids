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
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeff.lim.wimk.manager.FirebaseTokenManager
import com.jeff.lim.wimk.screen.RegisterMainView
import com.jeff.lim.wimk.ui.theme.WIMKTheme
import com.jeff.lim.wimk.viewmodel.FirebaseTokenViewModel
import com.naver.maps.map.compose.ExperimentalNaverMapApi
import com.naver.maps.map.compose.NaverMap
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val logTag = "[WIMK]${this::class.java.simpleName}"
    private val fireBaseTokenViewModel: FirebaseTokenViewModel by viewModels()
    @Inject lateinit var firebaseTokenManager: FirebaseTokenManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Create token
            // If true, token is already existed.
            fireBaseTokenViewModel.updateAndroidId(getAndroidId())

            if (firebaseTokenManager.registerTokenManager()) {
                RegisterMainView(fireBaseTokenViewModel)
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun getAndroidId() = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
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
    RegisterMainView(FirebaseTokenViewModel())
}