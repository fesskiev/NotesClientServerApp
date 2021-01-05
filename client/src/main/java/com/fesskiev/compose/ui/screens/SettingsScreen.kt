package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.SettingsUiState
import com.fesskiev.compose.presentation.SettingsViewModel
import com.fesskiev.compose.ui.components.AppBackToolbar
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.components.SnackBar
import com.fesskiev.compose.ui.utils.Constants.ThemeMode.DAY
import com.fesskiev.compose.ui.utils.Constants.ThemeMode.NIGHT
import com.fesskiev.compose.ui.utils.Constants.ThemeMode.SYSTEM
import org.koin.androidx.compose.getViewModel

@Composable
fun SettingsScreen(
    navController: NavHostController,
    viewModel: SettingsViewModel = getViewModel()
) {
    val uiState = viewModel.stateFlow.collectAsState().value
    when {
        uiState.isLogout -> navController.navigate("auth")
        uiState.loading -> ProgressBar()
        else -> {
            Scaffold(topBar = {
                AppBackToolbar(stringResource(R.string.settings)) {
                    navController.popBackStack()
                }
            }, bodyContent = {
                SettingsContent(
                    uiState,
                    onHidePopupClick = { viewModel.hideThemeModePopup() },
                    onShowPopupClick = { viewModel.showThemeModePopup() },
                    onThemeClick = { viewModel.setThemeMode(it) },
                    onLogoutClick = { viewModel.logout() })
            })
            uiState.errorResourceId?.let {
                SnackBar(stringResource(it))
            }
        }
    }
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onHidePopupClick: () -> Unit,
    onShowPopupClick: () -> Unit,
    onThemeClick: (String) -> Unit,
    onLogoutClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        if (uiState.isThemeModePopupShow) {
            ThemePopup(
                uiState = uiState,
                onDismissClick = onHidePopupClick,
                onThemeClick = onThemeClick
            )
        }
        Row(
            modifier = Modifier.preferredHeight(64.dp).clickable(onClick = onShowPopupClick),
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

@Composable
private fun ThemePopup(
    uiState: SettingsUiState,
    onDismissClick: () -> Unit,
    onThemeClick: (String) -> Unit
) {
    Popup(
        alignment = Alignment.Center,
        onDismissRequest = { onDismissClick() }
    ) {
        Card(
            modifier = Modifier.preferredSize(width = 220.dp, height = 140.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                arrayOf(
                    Theme(
                        themeMode = NIGHT,
                        text = stringResource(R.string.theme_night),
                        selected = uiState.themeMode == NIGHT
                    ),
                    Theme(
                        themeMode = DAY,
                        text = stringResource(R.string.theme_day),
                        selected = uiState.themeMode == DAY
                    ),
                    Theme(
                        themeMode = SYSTEM,
                        text = stringResource(R.string.theme_system),
                        selected = uiState.themeMode == SYSTEM
                    )
                ).forEach { theme ->
                    RadioButtonRow(text = theme.text, selected = theme.selected, onClick = {
                        onThemeClick(theme.themeMode)
                    })
                    Spacer(Modifier.preferredHeight(12.dp))
                }
            }
        }
    }
}

@Composable
private fun RadioButtonRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        RadioButton(
            modifier = Modifier.padding(start = 12.dp),
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 12.dp).fillMaxWidth(),
            style = MaterialTheme.typography.body1
        )
    }
}


data class Theme(val themeMode: String, val text: String, val selected: Boolean)