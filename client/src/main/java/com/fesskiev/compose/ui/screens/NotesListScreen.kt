package com.fesskiev.compose.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.fesskiev.compose.R
import com.fesskiev.compose.state.NotesUiState
import com.fesskiev.compose.ui.components.PagingProgressBar
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.utils.formatDate
import com.fesskiev.compose.ui.utils.toPictureUrl
import com.fesskiev.model.Note

@Composable
fun NotesListScreen(
    uiState: NotesUiState,
    onNoteClick: (Note) -> Unit,
    onNoteDeleteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit,
    onLoadMore: () -> Unit,
    onRetryClick: () -> Unit
) {
    val scrollState = rememberLazyListState()
    val notes = uiState.notes
    when {
        notes == null -> {
            if (uiState.loading) {
                ProgressBar()
            }
        }
        notes.isEmpty() -> EmptyNotesList()
        else -> {
            LazyColumn(modifier = Modifier.fillMaxSize(), state = scrollState) {
                items(notes) { note ->
                    NoteItem(note, onNoteClick, onNoteDeleteClick, onNoteEditClick)
                }
                if (uiState.error != null) {
                    item {
                        RetryButton(onRetryClick = onRetryClick)
                    }
                } else if (uiState.loadMore) {
                    item {
                        PagingProgressBar()
                    }
                }
            }
        }
    }
    if (scrollState.isScrolledToTheEnd() &&
        !uiState.endOfPaginationReached &&
        !uiState.loadMore &&
        uiState.error == null
    ) {
        LaunchedEffect(Unit) {
            onLoadMore()
        }
    }
}

@Composable
fun RetryButton(onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedButton(onClick = onRetryClick) {
            Text(text = stringResource(R.string.retry))
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

fun LazyListState.isScrolledToTheEnd(): Boolean =
    layoutInfo.visibleItemsInfo.lastOrNull()?.index == layoutInfo.totalItemsCount - 1