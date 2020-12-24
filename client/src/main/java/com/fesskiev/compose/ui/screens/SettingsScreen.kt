package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fesskiev.compose.R
import com.fesskiev.compose.ui.components.AppBackToolbar

@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(topBar = {
        AppBackToolbar(stringResource(R.string.settings)) {
            navController.popBackStack()
        }
    }, bodyContent = { SettingsContent() })
}

@Composable
private fun SettingsContent() {
    Column(modifier = Modifier.fillMaxSize()) {
        Spacer(Modifier.preferredHeight(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.dark_theme),
                modifier = Modifier.padding(start = 16.dp).weight(1f)
                    .wrapContentWidth(Alignment.Start),
                style = MaterialTheme.typography.body1
            )
            val checkedState = remember { mutableStateOf(true) }
            Switch(
                checked = checkedState.value,
                modifier = Modifier.padding(end = 16.dp).weight(1f)
                    .wrapContentWidth(Alignment.End),
                onCheckedChange = { checkedState.value = it }
            )
        }
    }
}