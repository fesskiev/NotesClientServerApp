package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R

@Composable
fun ExitDialog(onCloseAppClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = stringResource(R.string.close_app_title),
                style = MaterialTheme.typography.subtitle2
            )
            Button(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .height(42.dp)
                    .align(Alignment.End),
                onClick = onCloseAppClick
            ) {
                Text(stringResource(R.string.yes))
            }
        }
    }
}