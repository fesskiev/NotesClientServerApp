package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R
import com.fesskiev.compose.model.Note
import com.fesskiev.compose.state.SearchNotesUiState
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.utils.formatDate
import com.fesskiev.compose.ui.utils.toPictureUrl

@Composable
fun NotesSearchScreen(uiState: SearchNotesUiState) {
    val notes = uiState.notes
    if (notes != null) {
        when {
            uiState.loading -> ProgressBar()
            notes.isEmpty() -> EmptySearchNotesList()
            else -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(notes) { note -> NoteSearchItem(note) }
                }
            }
        }
    }
}

@Composable
fun NoteSearchItem(note: Note) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 64.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(Modifier.height(8.dp))
            note.pictureName?.let {
                NoteItemPicture(it.toPictureUrl())
            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = note.time.formatDate(),
                modifier = Modifier.align(Alignment.End),
                style = MaterialTheme.typography.overline
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun EmptySearchNotesList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.empty_search_notes_list),
            style = MaterialTheme.typography.h5
        )
    }
}
