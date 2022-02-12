package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R
import com.fesskiev.compose.state.EditNoteUiState
import com.fesskiev.compose.ui.components.AsciiTextField
import com.fesskiev.compose.ui.components.ProgressBar

@Composable
fun EditNoteScreen(
    uiState: EditNoteUiState,
    onEditedNoteClick: () -> Unit,
    onEditNoteChangedTitle: (String) -> Unit,
    onEditNoteChangedDescription: (String) -> Unit,
    onScreenClose: () -> Unit
) {
    when {
        uiState.loading -> ProgressBar()
        uiState.success -> { LaunchedEffect(Unit) { onScreenClose() } }
        else -> {
            EditNoteContent(
                uiState = uiState,
                onEditedClick = { onEditedNoteClick() },
                onChangedTitle = { onEditNoteChangedTitle(it) },
                onChangedDescription = { onEditNoteChangedDescription(it) }
            )
        }
    }
}

@Composable
fun EditNoteContent(
    uiState: EditNoteUiState,
    onEditedClick: () -> Unit,
    onChangedTitle: (String) -> Unit,
    onChangedDescription: (String) -> Unit
) {
    val titleLabel = when {
        uiState.title.isEmpty() -> stringResource(R.string.error_empty_title)
        else -> stringResource(R.string.note_title)
    }
    val descriptionLabel = when {
        uiState.description.isEmpty() -> stringResource(R.string.error_empty_desc)
        else -> stringResource(R.string.note_description)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(4.dp))
        AsciiTextField(
            label = titleLabel,
            value = uiState.title,
            textStyle = MaterialTheme.typography.subtitle1,
            onValueChange = onChangedTitle,
            isError = uiState.title.isEmpty()
        )
        Spacer(Modifier.height(20.dp))
        AsciiTextField(
            label = descriptionLabel,
            value = uiState.description,
            textStyle = MaterialTheme.typography.body2,
            onValueChange = onChangedDescription,
            isError = uiState.description.isEmpty(),
        )
        Button(
            modifier = Modifier
                .padding(top = 8.dp)
                .height(48.dp)
                .align(Alignment.End),
            onClick = onEditedClick
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}
