package com.jeff.lim.wimk.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jeff.lim.wimk.ui.theme.WIMKTheme

@Composable
fun KidsListScreen() {
    WIMKTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(10) {
                    KidsInfoCard()
                }
            }

            FloatingActionButton(
                onClick = { /*TODO*/ },
                shape = RoundedCornerShape(16.dp),
                backgroundColor = MaterialTheme.colors.secondary,
                modifier = Modifier.align(Alignment.BottomEnd).padding(end = 20.dp, bottom = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add FAB",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun KidsInfoCard() {
    Card(modifier = Modifier.fillMaxWidth().height(100.dp).padding(10.dp)) {
        Text(text = "TEST")
    }
}

@Preview(showBackground = true)
@Composable
fun KidsListScreenPreview() {
    KidsListScreen()
}



