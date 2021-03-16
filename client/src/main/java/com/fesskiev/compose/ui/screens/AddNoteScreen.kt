package com.fesskiev.compose.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.registerForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.fesskiev.compose.ui.theme.Grey
import dev.chrisbanes.accompanist.coil.CoilImage
import java.io.File

@Composable
fun AddNoteScreen(
    navController: NavHostController,
    viewModel: AddNoteViewModel = getViewModel()
) {
    var imageData by rememberSaveable { mutableStateOf<Pair<Bitmap?, File>?>(null) }
    val context = LocalContext.current
    val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let { data ->
                context.getBitmapFromIntent(data)?.let {
                    imageData = it
                }
            }
        }
    val uiState = viewModel.stateFlow.collectAsState().value
    when {
        uiState.addNoteState.success -> navController.popBackStack()
        else -> {
            AppScaffold(
                topBar = {
                    AppBackToolbar(
                        title = stringResource(R.string.add_note),
                        actions = {
                            AppToolbarButtons(
                                addNoteOnClick = {
                                    viewModel.addNote(
                                        uiState.title,
                                        uiState.description,
                                        imageData?.second
                                    )
                                },
                                pickImageOnClick = {
                                    launcher.launch(context.pickImageChooserIntent(title = "Pick Image"))
                                })
                        }) {
                        navController.popBackStack()
                    }
                },
                content = {
                    AddNoteContent(
                        uiState,
                        imageData?.first,
                        imageData?.second,
                        titleOnChange = { viewModel.changeTitle(it) },
                        descriptionOnChange = { viewModel.changeDescription(it) },
                        deleteImageOnClick = { imageData = null }
                    )
                },
                errorResourceId = uiState.errorResourceId
            )
        }
    }
}

@Composable
fun AppToolbarButtons(addNoteOnClick: () -> Unit, pickImageOnClick: () -> Unit) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_add),
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable(onClick = addNoteOnClick)
        )
        Image(
            painter = painterResource(R.drawable.ic_image),
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable(onClick = pickImageOnClick)
        )
    }
}

@Composable
fun AddNoteContent(
    uiState: AddNoteUiState,
    bitmap: Bitmap?,
    file: File?,
    titleOnChange: (String) -> Unit,
    descriptionOnChange: (String) -> Unit,
    deleteImageOnClick: () -> Unit
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
                    isError = uiState.addNoteState.isEmptyTitle
                )
                Spacer(Modifier.height(20.dp))
                AsciiTextField(
                    label = descriptionLabel,
                    value = uiState.description,
                    textStyle = MaterialTheme.typography.body2,
                    onValueChange = descriptionOnChange,
                    isError = uiState.addNoteState.isEmptyDescription,
                )
                if (bitmap != null && file != null) {
                    Spacer(Modifier.height(25.dp))
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = Alignment.Start,
                    ) {
                        ImageAttachment(bitmap, file, deleteImageOnClick = deleteImageOnClick)
                    }
                }
            }
        }
    }
}

@Composable
fun ImageAttachment(bitmap: Bitmap, file: File, deleteImageOnClick: () -> Unit) {
    Box {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp))
                .background(color = Grey),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CoilImage(
                data = bitmap,
                contentDescription = "",
                fadeIn = true,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp, 100.dp)
            )
            Text(
                text = file.name,
                modifier = Modifier.padding(4.dp),
                style = MaterialTheme.typography.body2.copy(color = Color.Black)
            )
        }
        Image(
            painter = painterResource(R.drawable.ic_cancel),
            contentDescription = "",
            modifier = Modifier
                .size(46.dp, 46.dp)
                .align(Alignment.TopEnd)
                .clickable {
                    deleteImageOnClick()
                }
        )
    }
}


@Preview
@Composable
fun ImageAttachmentPreview() {
    val file = File("./image.png")
    val bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565)
    ImageAttachment(bitmap, file, deleteImageOnClick = {})
}

