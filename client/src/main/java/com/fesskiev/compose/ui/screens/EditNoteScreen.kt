package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.EditNoteUiState
import com.fesskiev.compose.presentation.EditNoteViewModel
import com.fesskiev.compose.ui.components.*
import org.koin.androidx.compose.getViewModel

@Composable
fun EditNoteScreen(
    navController: NavHostController,
    viewModel: EditNoteViewModel = getViewModel(),
    noteUid: Int
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    val uiState = viewModel.stateFlow.collectAsState().value
    LaunchedEffect(noteUid) {
        viewModel.getNoteByUid(noteUid)
    }
    if (uiState.note != null) {
        val note = uiState.note
        title = note.title
        description = note.description
    }
    val titleLabel = when {
        uiState.editNoteState.isEmptyTitle -> stringResource(R.string.error_empty_title)
        else -> stringResource(R.string.note_title)
    }
    val descriptionLabel = when {
        uiState.editNoteState.isEmptyDescription -> stringResource(R.string.error_empty_desc)
        else -> stringResource(R.string.note_description)
    }
    if (uiState.editNoteState.success) {
        navController.popBackStack()
    } else {
        AppScaffold(
            topBar = {
                AppBackToolbar(stringResource(R.string.edit_note)) {
                    navController.popBackStack()
                }
            },
            content = {
                EditNoteContent(
                    uiState,
                    titleLabel,
                    descriptionLabel,
                    title,
                    description,
                    onEditClick = {
                        viewModel.editNote(noteUid, title, description)
                    },
                    titleOnChange = { title = it },
                    descriptionOnChange = { description = it })
            },
            errorResourceId = uiState.errorResourceId
        )
    }
}

@Composable
fun EditNoteContent(
    uiState: EditNoteUiState,
    titleLabel: String,
    descriptionLabel: String,
    title: String,
    description: String,
    onEditClick: () -> Unit,
    titleOnChange: (String) -> Unit,
    descriptionOnChange: (String) -> Unit
) {
    when {
        uiState.loading -> ProgressBar()
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsciiTextField(
                    label = titleLabel,
                    value = title,
                    onValueChange = titleOnChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    isError = uiState.editNoteState.isEmptyTitle
                )
                AsciiTextField(
                    label = descriptionLabel,
                    value = description,
                    onValueChange = descriptionOnChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    isError = uiState.editNoteState.isEmptyDescription,
                )
                Button(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .height(48.dp)
                        .align(Alignment.End),
                    onClick = onEditClick
                ) {
                    Text(stringResource(R.string.submit))
                }
            }
        }
    }
}
