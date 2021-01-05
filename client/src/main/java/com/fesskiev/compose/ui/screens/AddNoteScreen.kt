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
import com.fesskiev.compose.presentation.AddNoteViewModel
import com.fesskiev.compose.ui.components.AppBackToolbar
import com.fesskiev.compose.ui.components.AsciiTextField
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.components.SnackBar
import org.koin.androidx.compose.getViewModel

@Composable
fun AddNoteScreen(navController: NavHostController, viewModel: AddNoteViewModel = getViewModel()) {
    val titleState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val descriptionState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val pictureUrlState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    Scaffold(topBar = {
        AppBackToolbar(stringResource(R.string.add_note)) {
            navController.popBackStack()
        }
    }, bodyContent = {
        val uiState = viewModel.stateFlow.collectAsState().value
        when {
            uiState.loading -> ProgressBar()
            uiState.addNoteState.success -> navController.popBackStack()
            else -> {
                val titleLabel = when {
                    uiState.addNoteState.isEmptyTitle -> stringResource(R.string.error_empty_title)
                    else -> stringResource(R.string.note_title)
                }
                val descriptionLabel = when {
                    uiState.addNoteState.isEmptyDescription -> stringResource(R.string.error_empty_desc)
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
                        isErrorValue = uiState.addNoteState.isEmptyTitle
                    )
                    AsciiTextField(
                        label = descriptionLabel,
                        textFieldState = descriptionState,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        isErrorValue = uiState.addNoteState.isEmptyDescription,
                    )
                    AsciiTextField(
                        label = stringResource(R.string.note_picture_url),
                        textFieldState = pictureUrlState,
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                    )
                    Button(
                        modifier = Modifier.padding(top = 8.dp).height(48.dp).align(Alignment.End),
                        onClick = {
                            viewModel.addNote(
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


