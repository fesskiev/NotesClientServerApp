package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.EditNoteViewModel
import com.fesskiev.compose.ui.components.AppBackToolbar
import com.fesskiev.compose.ui.components.AsciiTextField
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.components.SnackBar
import org.koin.androidx.compose.getViewModel

@Composable
fun EditNoteScreen(
    navController: NavHostController,
    viewModel: EditNoteViewModel = getViewModel(),
    noteUid: Int
) {
    val titleState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val descriptionState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val pictureUrlState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    viewModel.getNoteByUid(noteUid)
    Scaffold(topBar = {
        AppBackToolbar(stringResource(R.string.edit_note)) {
            navController.popBackStack()
        }
    }, bodyContent = {
        val uiState = viewModel.stateFlow.collectAsState().value
        when {
            uiState.loading -> ProgressBar()
            uiState.editNoteState.success -> navController.popBackStack()
            else -> {
                if (uiState.note != null) {
                    val note = uiState.note
                    titleState.value = TextFieldValue(note.title)
                    descriptionState.value = TextFieldValue(note.description)
                    pictureUrlState.value = TextFieldValue(note.pictureUrl ?: "")
                }
                val titleLabel = when {
                    uiState.editNoteState.isEmptyTitle -> stringResource(R.string.error_empty_title)
                    else -> stringResource(R.string.note_title)
                }
                val descriptionLabel = when {
                    uiState.editNoteState.isEmptyDescription -> stringResource(R.string.error_empty_desc)
                    else -> stringResource(R.string.note_description)
                }
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsciiTextField(
                        label = titleLabel,
                        textFieldState = titleState,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        isErrorValue = uiState.editNoteState.isEmptyTitle
                    )
                    AsciiTextField(
                        label = descriptionLabel,
                        textFieldState = descriptionState,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        isErrorValue = uiState.editNoteState.isEmptyDescription,
                    )
                    AsciiTextField(
                        label = stringResource(R.string.note_picture_url),
                        textFieldState = pictureUrlState,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    Button(
                        modifier = Modifier.padding(top = 8.dp).height(48.dp).align(Alignment.End),
                        onClick = {
                            viewModel.editNote(
                                noteUid,
                                titleState.value.text,
                                descriptionState.value.text,
                                pictureUrlState.value.text
                            )
                        }
                    ) {
                        Text(stringResource(R.string.submit))
                    }
                }
                uiState.errorResourceId?.let {
                    SnackBar(stringResource(it))
                }
            }
        }
    })
}