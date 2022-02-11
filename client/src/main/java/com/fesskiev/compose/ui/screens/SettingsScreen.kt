package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R
import com.fesskiev.compose.state.SettingsUiState
import com.fesskiev.compose.presentation.SettingsViewModel
import com.fesskiev.compose.ui.components.ProgressBar
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = getViewModel(),
    onShowThemeDialogClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val uiState = viewModel.uiStateFlow.collectAsState().value
    when {
        uiState.isLogout -> {
            LaunchedEffect(Unit) {
                onLogoutClick()
            }
        }
        uiState.loading -> ProgressBar()
        else -> {
            SettingsContent(
                uiState,
                onShowThemeDialogClick = onShowThemeDialogClick,
                onLogoutClick = { viewModel.logout() })
            if (uiState.error != null) {

            }
        }
    }
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onShowThemeDialogClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .height(64.dp)
                .clickable(onClick = onShowThemeDialogClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.theme),
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start),
                style = MaterialTheme.typography.body1
            )
            Text(
                text = uiState.themeMode,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f)
                    .wrapContentWidth(Alignment.End),
                style = MaterialTheme.typography.body1
            )
        }
        Text(
            text = stringResource(R.string.logout),
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(bottom = 24.dp)
                .fillMaxSize()
                .wrapContentWidth(Alignment.CenterHorizontally)
                .wrapContentHeight(Alignment.Bottom)
                .clickable(onClick = onLogoutClick),
            textDecoration = TextDecoration.Underline
        )
    }
}
