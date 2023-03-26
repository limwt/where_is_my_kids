package com.jeff.lim.wimk.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jeff.lim.wimk.R
import com.jeff.lim.wimk.ui.theme.WIMKTheme

@Composable
fun NoRegisterScreen() {
    WIMKTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.text_register),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 50.dp),
                fontSize = 25.sp
            )

            Divider(
                color = Color.Transparent,
                thickness = 20.dp
            )

            Button(
                onClick = {
                    /* TODO */
                },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(3.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
                modifier = Modifier
                    .width(300.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 100.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.text_register_parent),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 6.dp)
                )
            }
            Divider(
                color = Color.Transparent,
                thickness = 20.dp
            )

            Button(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(3.dp, Color.Black),
                colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
                modifier = Modifier
                    .width(300.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = stringResource(id = R.string.text_register_children),
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 30.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NoRegisterScreenPreview() {
    NoRegisterScreen()
}