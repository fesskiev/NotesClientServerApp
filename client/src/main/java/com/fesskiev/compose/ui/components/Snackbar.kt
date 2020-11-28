package com.fesskiev.compose.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember

@Composable
fun SnackBar(message: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        val visibleState = remember { mutableStateOf(true) }
        if (visibleState.value) {
            Snackbar(
                text = { Text(text = message) },
                action = {
                    Button(onClick = {
                        visibleState.value = false
                    }) {
                        Text("Hide")
                    }
                },
            )
        }
    }
}