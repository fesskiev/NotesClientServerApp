package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.SettingsPresenter
import com.fesskiev.compose.ui.utils.ThemeMode.DAY
import com.fesskiev.compose.ui.utils.ThemeMode.NIGHT
import com.fesskiev.compose.ui.utils.ThemeMode.SYSTEM
import org.koin.androidx.compose.get

@Composable
fun ThemeDialog(presenter: SettingsPresenter = get()) {
    val uiState by presenter.settingsUiState
        Card(modifier = Modifier.fillMaxWidth()) {
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
                        presenter.setThemeMode(theme.themeMode)
                    })
                }
            }
        }
}

@Composable
private fun RadioButtonRow(text: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            modifier = Modifier.padding(start = 12.dp),
            selected = selected,
            onClick = onClick
        )
        Text(
            text = text,
            modifier = Modifier.padding(start = 12.dp),
            style = MaterialTheme.typography.body1
        )
    }
}


data class Theme(val themeMode: String, val text: String, val selected: Boolean)