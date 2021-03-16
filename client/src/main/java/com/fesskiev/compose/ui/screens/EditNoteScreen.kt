package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
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
    val uiState = viewModel.stateFlow.collectAsState().value
    LaunchedEffect(noteUid) {
        viewModel.getNoteByUid(noteUid)
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
                    onEditClick = {
                        viewModel.editNote(noteUid, uiState.title, uiState.description)
                    },
                    titleOnChange = { viewModel.changeTitle(it) },
                    descriptionOnChange = { viewModel.changeDescription(it) })
            },
            errorResourceId = uiState.errorResourceId
        )
    }
}

@Composable
fun EditNoteContent(
    uiState: EditNoteUiState,
    onEditClick: () -> Unit,
    titleOnChange: (String) -> Unit,
    descriptionOnChange: (String) -> Unit
) {
    when {
        uiState.loading -> ProgressBar()
        else -> {
            val titleLabel = when {
                uiState.editNoteState.isEmptyTitle -> stringResource(R.string.error_empty_title)
                else -> stringResource(R.string.note_title)
            }
            val descriptionLabel = when {
                uiState.editNoteState.isEmptyDescription -> stringResource(R.string.error_empty_desc)
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
                    onValueChange = titleOnChange,
                    isError = uiState.editNoteState.isEmptyTitle
                )
                Spacer(Modifier.height(20.dp))
                AsciiTextField(
                    label = descriptionLabel,
                    value = uiState.description,
                    textStyle = MaterialTheme.typography.body2,
                    onValueChange = descriptionOnChange,
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
