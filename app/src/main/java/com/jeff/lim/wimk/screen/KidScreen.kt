package com.jeff.lim.wimk.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jeff.lim.wimk.ui.theme.WIMKTheme

@Composable
fun KidScreen() {
    WIMKTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "자녀 화면 구현 중...")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KidScreenPreview() {
    KidScreen()
}



