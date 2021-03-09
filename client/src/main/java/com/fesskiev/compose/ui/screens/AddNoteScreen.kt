package com.fesskiev.compose.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import org.koin.androidx.compose.getViewModel
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.fesskiev.compose.ui.utils.stateSaver
import dev.chrisbanes.accompanist.coil.CoilImage
import java.io.File

@Composable
fun AddNoteScreen(
    navController: NavHostController,
    viewModel: AddNoteViewModel = getViewModel()
) {
    var title by rememberSaveable(saver = stateSaver()) { mutableStateOf("") }
    var description by rememberSaveable(saver = stateSaver()) { mutableStateOf("") }
    var pair by rememberSaveable(saver = stateSaver()) { mutableStateOf<Pair<Bitmap, File>?>(null) }
    val context = LocalContext.current
    val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
            content = {
                AddNoteContent(uiState, title, description, pair?.first,
                    onPickImageClick = {
                        launcher.launch(context.pickImageChooserIntent(title = "Pick Image"))
                    },
                    titleOnChange = { title = it },
                    descriptionOnChange = { description = it })
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        viewModel.addNote(title, description, pair?.second)
                    }
                ) {
                    Image(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = ""
                    )
                }
            },
            errorResourceId = uiState.errorResourceId
        )
    }
}

@Composable
fun AddNoteContent(
    uiState: AddNoteUiState,
    title: String,
    description: String,
    bitmap: Bitmap?,
    onPickImageClick: () -> Unit,
    titleOnChange: (String) -> Unit,
    descriptionOnChange: (String) -> Unit
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
                    value = title,
                    onValueChange = titleOnChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    isError = uiState.addNoteState.isEmptyTitle
                )
                AsciiTextField(
                    label = descriptionLabel,
                    value = description,
                    onValueChange = descriptionOnChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(top = 8.dp),
                    isError = uiState.addNoteState.isEmptyDescription,
                )
                Image(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(36.dp, 36.dp)
                        .align(Alignment.End)
                        .clickable {
                            onPickImageClick()
                        },
                    painter = painterResource(R.drawable.ic_image),
                    contentDescription = ""
                )
                bitmap?.let {
                    CoilImage(
                        data = it,
                        contentDescription = "",
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


