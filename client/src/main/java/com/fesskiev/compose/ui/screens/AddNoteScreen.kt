package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.fesskiev.compose.R
import com.fesskiev.compose.state.AddNoteUiState
import com.fesskiev.compose.ui.components.AsciiTextField
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.theme.Grey
import java.io.File

@Composable
fun AddNoteScreen(
    uiState: AddNoteUiState,
    onAddNoteChangedTitle: (String) -> Unit,
    onAddNoteChangedDescription: (String) -> Unit,
    onDeleteImageClick: () -> Unit,
    onScreenClose: () -> Unit
) {
    when {
        uiState.loading -> ProgressBar()
        uiState.success -> LaunchedEffect(Unit) { onScreenClose() }
        else -> {
            AddNoteContent(
                uiState = uiState,
                onChangedTitle = { onAddNoteChangedTitle(it) },
                onChangedDescription = { onAddNoteChangedDescription(it) },
                onDeleteImageClick = { onDeleteImageClick() }
            )
        }
    }
}

@Composable
fun AddNoteContent(
    uiState: AddNoteUiState,
    onChangedTitle: (String) -> Unit,
    onChangedDescription: (String) -> Unit,
    onDeleteImageClick: () -> Unit
) {
    val isEmptyTitle = uiState.addNoteUserInputState.isEmptyTitleError
    val isEmptyDescription = uiState.addNoteUserInputState.isEmptyDescriptionError
    val titleLabel = when {
        isEmptyTitle -> stringResource(R.string.error_empty_title)
        else -> stringResource(R.string.note_title)
    }
    val descriptionLabel = when {
        isEmptyDescription -> stringResource(R.string.error_empty_desc)
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
            isError = isEmptyTitle
        )
        Spacer(Modifier.height(20.dp))
        AsciiTextField(
            label = descriptionLabel,
            value = uiState.description,
            textStyle = MaterialTheme.typography.body2,
            onValueChange = onChangedDescription,
            isError = isEmptyDescription,
        )
        val file = uiState.imageFile
        if (file != null) {
            Spacer(Modifier.height(25.dp))
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.Start,
            ) {
                ImageAttachment(file, deleteImageOnClick = onDeleteImageClick)
            }
        }
    }
}

@Composable
fun ImageAttachment(imageFile: File, deleteImageOnClick: () -> Unit) {
    Box {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp, 8.dp, 8.dp, 8.dp))
                .background(color = Grey),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberImagePainter(
                    data = imageFile,
                    onExecute = { _, _ -> true },
                    builder = {
                        crossfade(true)
                    }
                ),
                contentScale = ContentScale.Crop,
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )
            Text(
                text = imageFile.name,
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

