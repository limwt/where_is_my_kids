package com.jeff.lim.wimk.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeff.lim.wimk.R
import com.jeff.lim.wimk.di.FirebaseDbRepository
import com.jeff.lim.wimk.viewmodel.UsersViewModel

@Composable
fun AuthKeyScreen(usersViewModel: UsersViewModel) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val authKey by remember {
            mutableStateOf(getRandomString())
        }

        if (authKey.isNotEmpty()) {
            usersViewModel.updateAuthKey(authKey)
        }

        Text(
            text = getRandomString(),
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(id = R.string.text_register_child),
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = {

            },
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(3.dp, Color.Black),
            colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
            modifier = Modifier
                .width(150.dp)
                .padding(top = 100.dp, end = 5.dp),
            enabled = false
        ) {
            Text(
                text = stringResource(id = R.string.button_save_text),
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 30.dp, vertical = 6.dp)
            )
        }
    }
}

private fun getRandomString() : String {
    val charset = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    return (1..8)
        .map { charset.random() }
        .joinToString("")
}

@Preview(showBackground = true)
@Composable
fun AuthKeyScreenPreview() {
    AuthKeyScreen(UsersViewModel(FirebaseDbRepository()))
}
