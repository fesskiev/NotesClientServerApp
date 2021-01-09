package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.AddNoteUiState
import com.fesskiev.compose.presentation.AddNoteViewModel
import com.fesskiev.compose.ui.components.AppBackToolbar
import com.fesskiev.compose.ui.components.AsciiTextField
import com.fesskiev.compose.ui.components.ProgressBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

@ExperimentalMaterialApi
@Composable
fun AddNoteScreen(navController: NavHostController, viewModel: AddNoteViewModel = getViewModel()) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val titleState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val descriptionState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val pictureUrlState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val uiState = viewModel.stateFlow.collectAsState().value
    if (uiState.addNoteState.success) {
        navController.popBackStack()
    } else {
        Scaffold(scaffoldState = scaffoldState,
            topBar = {
                AppBackToolbar(stringResource(R.string.add_note)) {
                    navController.popBackStack()
                }
            }, bodyContent = {
                AddNoteContent(
                    titleState,
                    descriptionState,
                    pictureUrlState,
                    uiState,
                    onAddClick = {
                        viewModel.addNote(
                            titleState.value.text,
                            descriptionState.value.text,
                            pictureUrlState.value.text
                        )
                    })
            })
        uiState.errorResourceId?.let {
            val message = stringResource(it)
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(message = message)
            }
        }
    }
}

@Composable
fun AddNoteContent(
    titleState: MutableState<TextFieldValue>,
    descriptionState: MutableState<TextFieldValue>,
    pictureUrlState: MutableState<TextFieldValue>,
    uiState: AddNoteUiState,
    onAddClick: () -> Unit
) {
    when {
        uiState.loading -> ProgressBar()
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsciiTextField(
                    label = titleLabel,
                    textFieldState = titleState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    isErrorValue = uiState.addNoteState.isEmptyTitle
                )
                AsciiTextField(
                    label = descriptionLabel,
                    textFieldState = descriptionState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    isErrorValue = uiState.addNoteState.isEmptyDescription,
                )
                AsciiTextField(
                    label = stringResource(R.string.note_picture_url),
                    textFieldState = pictureUrlState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                )
                Button(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .height(48.dp)
                        .align(Alignment.End),
                    onClick = onAddClick
                ) {
                    Text(stringResource(R.string.submit))
                }
            }
        }
    }
}


