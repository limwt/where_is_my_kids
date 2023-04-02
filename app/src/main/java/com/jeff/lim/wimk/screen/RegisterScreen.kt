package com.jeff.lim.wimk.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.jeff.lim.wimk.R
import com.jeff.lim.wimk.database.RoleType
import com.jeff.lim.wimk.ui.theme.WIMKTheme
import com.jeff.lim.wimk.viewmodel.FirebaseTokenViewModel

@Composable
fun RegisterScreen(navController: NavController, firebaseTokenViewModel: FirebaseTokenViewModel) {
    WIMKTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            var nameText by remember { mutableStateOf("") }
            var dialogState by remember { mutableStateOf(false) }
            var relationIndex by remember { mutableStateOf(-1) }
            val relationList = listOf(RoleType.Mom, RoleType.Dad, RoleType.Son, RoleType.Daughter, RoleType.Other)

            if (dialogState) {
                SelectRelationDialog(
                    defaultSelected = relationIndex,
                    onDismissRequest = {
                        relationIndex = it
                        dialogState = false
                    }
                )
            }

            Box(modifier = Modifier.fillMaxSize().padding(top = 100.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.TopCenter)
                        .padding(top = 50.dp, bottom = 50.dp)
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.text_name),
                            fontSize = 25.sp,
                            modifier = Modifier.align(Alignment.CenterVertically).padding(start = 50.dp)
                        )

                        TextField(
                            value = nameText,
                            onValueChange = { nameText = it },
                            modifier = Modifier.fillMaxWidth().padding(start = 50.dp, end = 50.dp)
                        )
                    }

                    Divider(
                        color = Color.Transparent,
                        thickness = 20.dp
                    )

                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.text_register_relation),
                            fontSize = 25.sp,
                            modifier = Modifier.align(Alignment.CenterVertically).padding(start = 50.dp)
                        )

                        Button(
                            onClick = { dialogState = !dialogState },
                            colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 50.dp, end = 50.dp)
                        ) {
                            Text(
                                text = if (relationIndex == -1) stringResource(id = R.string.text_no_relation) else relationList[relationIndex].role,
                                fontSize = 20.sp
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(10.dp)
                ) {
                    Button(
                        onClick = {
                            firebaseTokenViewModel.requestToken(
                                role = relationList[relationIndex].name,
                                name = nameText
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(3.dp, Color.Black),
                        colors = ButtonDefaults.buttonColors(contentColor = Color.Black, backgroundColor = Color.White),
                        modifier = Modifier
                            .width(150.dp)
                            .weight(1.0f)
                            .padding(end = 5.dp),
                        enabled = relationIndex >= 0 && nameText.isNotEmpty()
                    ) {
                        Text(
                            text = stringResource(id = R.string.button_save_text),
                            fontSize = 20.sp,
                            modifier = Modifier.padding(horizontal = 30.dp, vertical = 6.dp)
                        )
                    }

                    Button(
                        onClick = {
                            navController.navigate(ScreenType.NewRegisterScreen.name) {
                                popUpTo(ScreenType.NewRegisterScreen.name) {
                                    inclusive = true
                                }
                            }
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

@Composable
fun SelectRelationDialog(defaultSelected: Int,
                         onDismissRequest: (Int) -> Unit) {
    var selectedOption by remember { mutableStateOf(defaultSelected) }

    Dialog(onDismissRequest = { onDismissRequest.invoke(defaultSelected) }) {
        Surface(
            modifier = Modifier.width(300.dp),
            shape = RoundedCornerShape(10.dp)
        ) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = stringResource(id = R.string.text_register_relation),
                    fontSize = 25.sp
                )

                Divider(
                    color = Color.Gray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)
                )

                LazyColumn {
                    val list = listOf("부모", "자녀")
                    itemsIndexed(items = list) { index, item ->
                        RadioButton(
                            text = item,
                            selectedValue = if (selectedOption == -1) list[0] else list[selectedOption],
                        ) {
                            selectedOption = index
                            onDismissRequest.invoke(index)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RadioButton(text: String, selectedValue: String, onSelected: (String) -> Unit) {
    Row(Modifier
        .fillMaxWidth()
        .selectable(
            selected = (text == selectedValue),
            onClick = {
                onSelected(text)
            }
        )
    ) {
        RadioButton(
            modifier = Modifier.align(Alignment.CenterVertically),
            selected = (text == selectedValue),
            onClick = {
                onSelected(text)
            }
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body1.merge(),
            modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoRegisterScreenPreview() {
    val navController = rememberNavController()
    val viewModel = FirebaseTokenViewModel()
    RegisterScreen(navController, viewModel)
}