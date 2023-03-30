package com.jeff.lim.wimk.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jeff.lim.wimk.R
import com.jeff.lim.wimk.ui.theme.WIMKTheme

@Composable
fun RegisterParentScreen(navController: NavController) {
    WIMKTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            var relationText by remember { mutableStateOf("") }
            var phoneNumberText by remember { mutableStateOf("") }

            //ParentRegisterView
            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.TopCenter)
                        .padding(top = 50.dp, bottom = 50.dp)
                ) {
                    TextField(
                        value = relationText,
                        onValueChange = { relationText = it },
                        label = { Text(text = stringResource(id = R.string.text_register_parent_relation)) },
                        placeholder = { Text(text = stringResource(id = R.string.text_register_parent_hint)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )

                    Divider(
                        color = Color.Transparent,
                        thickness = 20.dp
                    )

                    TextField(
                        value = phoneNumberText,
                        onValueChange = { phoneNumberText = it },
                        label = { Text(text = stringResource(id = R.string.text_phone_number)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(10.dp)
                ) {
                    Button(
                        onClick = {
                            //visibleRegisterView = false
                            //viewModel.requestToken(relationText, phoneNumberText)
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(3.dp, Color.Black),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
                        modifier = Modifier
                            .width(150.dp)
                            .weight(1.0f)
                            .padding(end = 5.dp),
                        enabled = relationText.isNotEmpty() && phoneNumberText.isNotEmpty()
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_save_text),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(horizontal = 30.dp, vertical = 6.dp)
                        )
                    }

                    Button(
                        onClick = {
                            //visibleRegisterView = false
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(3.dp, Color.Black),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
                        modifier = Modifier
                            .width(150.dp)
                            .weight(1.0f)
                            .padding(start = 5.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_cancel_text),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(horizontal = 30.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterParentScreenPreview() {
    RegisterParentScreen(rememberNavController())
}
