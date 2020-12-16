package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.AddNoteViewModel
import com.fesskiev.compose.ui.components.AppBackToolbar
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.components.SnackBar
import org.koin.androidx.compose.getViewModel

@Composable
fun AddNoteScreen(navController: NavHostController, viewModel: AddNoteViewModel = getViewModel()) {
    val title = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val description = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val pictureUrl = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    Scaffold(topBar = {
        AppBackToolbar(stringResource(R.string.add_note)) {
            navController.popBackStack()
        }
    }, bodyContent = {
        val uiState = viewModel.stateFlow.collectAsState().value
        when {
            uiState.loading -> ProgressBar()
            uiState.success -> navController.popBackStack()
            else -> {
                val titleLabel = when {
                    uiState.isEmptyTitle -> stringResource(R.string.error_empty_title)
                    else -> stringResource(R.string.note_title)
                }
                val descriptionLabel = when {
                    uiState.isEmptyDescription -> stringResource(R.string.error_empty_desc)
                    else -> stringResource(R.string.note_description)
                }
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        value = title.value,
                        onValueChange = { title.value = it },
                        label = { Text(titleLabel) },
                        isErrorValue = uiState.isEmptyTitle,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        value = description.value,
                        onValueChange = { description.value = it },
                        label = { Text(descriptionLabel) },
                        isErrorValue = uiState.isEmptyDescription,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                        value = pictureUrl.value,
                        onValueChange = { pictureUrl.value = it },
                        label = { Text(stringResource(R.string.note_picture_url)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii)
                    )
                    Button(
                        modifier = Modifier.padding(top = 8.dp).height(48.dp).align(Alignment.End),
                        onClick = {
                            viewModel.addNote(
                                title.value.text,
                                description.value.text,
                                pictureUrl.value.text
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
