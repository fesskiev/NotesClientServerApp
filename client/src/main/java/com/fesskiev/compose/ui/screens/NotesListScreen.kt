package com.fesskiev.compose.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.fesskiev.compose.R
import com.fesskiev.compose.paging.isEmpty
import com.fesskiev.compose.ui.components.PagingProgressBar
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.utils.formatDate
import com.fesskiev.compose.ui.utils.toPictureUrl
import com.fesskiev.model.Note

@Composable
fun NotesListScreen(
    notesItems: LazyPagingItems<Note>,
    loading: Boolean,
    onNoteClick: (Note) -> Unit,
    onNoteDeleteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit
) {
    if (notesItems.isEmpty()) {
        if (loading) {
            ProgressBar()
        } else {
            EmptyNotesList()
        }
    } else {
        LazyColumn {
            items(notesItems) { note ->
                NoteItem(note!!, onNoteClick, onNoteDeleteClick, onNoteEditClick)
            }
            if (loading) {
                item { PagingProgressBar() }
            }
        }
    }
}

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun NoteItem(
    note: Note,
    onNoteClick: (Note) -> Unit,
    onNoteDeleteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .defaultMinSize(minHeight = 64.dp)
            .combinedClickable(
                onClick = { onNoteClick(note) },
                onLongClick = { expanded = !expanded }
            ),
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
            AnimatedVisibility(visible = expanded) {
                NoteItemBottomMenu(
                    note,
                    onNoteDeleteClick,
                    onNoteEditClick,
                    toggleMenuVisibility = { expanded = !expanded }
                )
            }
        }
    }
}

@Composable
private fun NoteItemPicture(url: String) {
    Image(
        painter = rememberImagePainter(
            data = url,
            onExecute = { _, _ -> true },
            builder = {
                crossfade(true)
            }
        ),
        contentScale = ContentScale.Crop,
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp),
    )
}

@Composable
private fun NoteItemBottomMenu(
    note: Note,
    onNoteDeleteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit,
    toggleMenuVisibility: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.ic_delete),
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable(
                    onClick = {
                        toggleMenuVisibility()
                        onNoteDeleteClick(note)
                    }
                )
        )
        Image(
            painter = painterResource(R.drawable.ic_edit),
            contentDescription = "",
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .clickable(
                    onClick = {
                        toggleMenuVisibility()
                        onNoteEditClick(note)
                    }
                )
        )
    }
}

@Composable
fun EmptyNotesList() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = stringResource(R.string.empty_notes_list), style = MaterialTheme.typography.h5)
    }
}