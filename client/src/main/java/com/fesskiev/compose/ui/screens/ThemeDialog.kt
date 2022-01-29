package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.SettingsViewModel
import com.fesskiev.compose.ui.utils.Constants
import org.koin.androidx.compose.getViewModel

@Composable
fun ThemeDialog(
    viewModel: SettingsViewModel = getViewModel(),
    onDismissClick: () -> Unit
) {
    val uiState = viewModel.uiStateFlow.collectAsState().value
    Dialog(
        onDismissRequest = { onDismissClick() }
    ) {
        Card(
            modifier = Modifier.size(width = 220.dp, height = 140.dp)
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                arrayOf(
                    Theme(
                        themeMode = Constants.ThemeMode.NIGHT,
                        text = stringResource(R.string.theme_night),
                        selected = uiState.themeMode == Constants.ThemeMode.NIGHT
                    ),
                    Theme(
                        themeMode = Constants.ThemeMode.DAY,
                        text = stringResource(R.string.theme_day),
                        selected = uiState.themeMode == Constants.ThemeMode.DAY
                    ),
                    Theme(
                        themeMode = Constants.ThemeMode.SYSTEM,
                        text = stringResource(R.string.theme_system),
                        selected = uiState.themeMode == Constants.ThemeMode.SYSTEM
                    )
                ).forEach { theme ->
                    RadioButtonRow(text = theme.text, selected = theme.selected, onClick = {
                        viewModel.setThemeMode(theme.themeMode)
                    })
                    Spacer(Modifier.height(12.dp))
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
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.body1
        )
    }
}


data class Theme(val themeMode: String, val text: String, val selected: Boolean)