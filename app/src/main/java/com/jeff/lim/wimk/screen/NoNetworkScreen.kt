package com.jeff.lim.wimk.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeff.lim.wimk.R
import com.jeff.lim.wimk.ui.theme.WIMKTheme

@Composable
fun NoNetworkScreen() {
    WIMKTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.text_not_connected_network),
                modifier = Modifier.align(Alignment.Center).padding(top = 50.dp),
                fontSize = 25.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoNetworkScreenPreview() {
    NoNetworkScreen()
}