package com.fesskiev.compose.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R

@Composable
fun AppDrawer(onSettingsClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.preferredHeight(24.dp))
        TextButton(onClick = { onSettingsClick() }, modifier = Modifier.fillMaxWidth()) {
            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
                Image(imageVector = vectorResource(R.drawable.ic_settings))
                Spacer(Modifier.preferredWidth(16.dp))
                Text(
                    text = stringResource(R.string.settings),
                    modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}