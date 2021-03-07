package com.fesskiev.compose.ui.screens

import android.graphics.Bitmap
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.AddNoteUiState
import com.fesskiev.compose.presentation.AddNoteViewModel
import com.fesskiev.compose.ui.components.AppBackToolbar
import com.fesskiev.compose.ui.components.AppScaffold
import com.fesskiev.compose.ui.components.AsciiTextField
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.utils.getBitmapFromIntent
import com.fesskiev.compose.ui.utils.pickImageChooserIntent
import com.fesskiev.compose.ui.utils.registerForActivityResult
import org.koin.androidx.compose.getViewModel
import androidx.compose.runtime.*
import androidx.compose.ui.layout.ContentScale
import dev.chrisbanes.accompanist.coil.CoilImage
import java.io.File

@Composable
fun AddNoteScreen(
    navController: NavHostController,
    viewModel: AddNoteViewModel = getViewModel()
) {
    val titleState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val descriptionState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    var pair by remember { mutableStateOf<Pair<Bitmap, File>?>(null) }
    val context = AmbientContext.current
    val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let { data ->
                context.getBitmapFromIntent(data)?.let {
                    pair = it
                }
            }
        }
    val uiState = viewModel.stateFlow.collectAsState().value
    if (uiState.addNoteState.success) {
        navController.popBackStack()
    } else {
        AppScaffold(
            topBar = {
                AppBackToolbar(stringResource(R.string.add_note)) {
                    navController.popBackStack()
                }
            },
            bodyContent = {
                AddNoteContent(
                    titleState,
                    descriptionState,
                    pair?.first,
                    uiState,
                    onPickImageClick = {
                        launcher.launch(context.pickImageChooserIntent(title = "Pick Image"))
                    })
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.addNote(
                            titleState.value.text,
                            descriptionState.value.text,
                            pair?.second
                        )
                    }
                ) {
                    Image(imageVector = vectorResource(R.drawable.ic_add))
                }
            },
            errorResourceId = uiState.errorResourceId
        )
    }
}

@Composable
fun AddNoteContent(
    titleState: MutableState<TextFieldValue>,
    descriptionState: MutableState<TextFieldValue>,
    bitmap: Bitmap?,
    uiState: AddNoteUiState,
    onPickImageClick: () -> Unit
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
                        .height(250.dp)
                        .padding(top = 8.dp),
                    isErrorValue = uiState.addNoteState.isEmptyDescription,
                )
                Image(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(36.dp, 36.dp)
                        .align(Alignment.End)
                        .clickable {
                            onPickImageClick()
                        },
                    imageVector = vectorResource(R.drawable.ic_image)
                )
                bitmap?.let {
                    CoilImage(
                        data = it,
                        fadeIn = true,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(200.dp, 200.dp)
                            .align(Alignment.Start)
                    )
                }
            }
        }
    }
}


