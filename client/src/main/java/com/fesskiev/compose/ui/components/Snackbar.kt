package com.fesskiev.compose.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Snackbar
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import com.fesskiev.compose.R

@Composable
fun SnackBar(message: String, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        val visibleState = remember { mutableStateOf(true) }
        if (visibleState.value) {
            Snackbar(
                text = { Text(text = message, style = MaterialTheme.typography.body1) },
                action = {
                    Button(onClick = {
                        visibleState.value = false
                        onClick()
                    }) {
                        Text(
                            text = stringResource(R.string.hide),
                            style = MaterialTheme.typography.body1
                        )
                    }
                },
            )
        }
    }
}