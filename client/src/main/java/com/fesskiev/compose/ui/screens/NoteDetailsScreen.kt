package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.fesskiev.compose.state.NotesListUiState
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.utils.formatDate
import com.fesskiev.compose.ui.utils.toPictureUrl
import com.fesskiev.model.Note

@Composable
fun NoteDetailsScreen(uiState: NotesListUiState) {
    when {
        uiState.loading -> ProgressBar()
        uiState.selectedNote != null -> NoteDetails(uiState.selectedNote)
    }
}

@Composable
fun NoteDetails(note: Note) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(9.dp))
        Text(
            text = note.time.formatDate(),
            modifier = Modifier.align(Alignment.End),
            style = MaterialTheme.typography.body2
        )
        Spacer(Modifier.height(9.dp))
        Text(text = note.title, style = MaterialTheme.typography.h5)
        Spacer(Modifier.height(25.dp))
        Text(text = note.description, style = MaterialTheme.typography.body1)
        Spacer(Modifier.height(25.dp))
        note.pictureName?.let {
            NoteDetailsPicture(it.toPictureUrl())
        }
    }
}

@Composable
private fun NoteDetailsPicture(url: String) {
    Image(
        painter = rememberImagePainter(
            data = url,
            onExecute = { _, _ -> true },
            builder = {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        ),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier.size(300.dp)
    )
}
