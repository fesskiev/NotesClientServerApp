package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R
import com.fesskiev.compose.ui.components.PagingProgressBar
import com.fesskiev.compose.ui.utils.formatDate
import com.fesskiev.model.Note

@Composable
fun NotesListScreen(
    notes: List<Note>,
    isPaging: Boolean,
    isNeedLoadMore: Boolean,
    onNoteClick: (Note) -> Unit,
    onNoteDeleteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit,
    onLoadMoreItems: () -> Unit
) {
    LazyColumn {
        itemsIndexed(notes) { index, note ->
            NoteItem(note, onNoteClick, onNoteDeleteClick, onNoteEditClick)
            if (isNeedLoadMore && notes.lastIndex == index) {
                if (isPaging) {
                    PagingProgressBar()
                } else {
                    onLoadMoreItems()
                }
            }
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onNoteClick: (Note) -> Unit,
    onNoteDeleteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit
) {
    val height = 100.dp
    BoxWithConstraints {
        val max = 0f
        val min = -160f
        val offsetPositionX = remember { mutableStateOf(0f) }
        Box(
            Modifier.draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    val newValue = offsetPositionX.value + delta
                    offsetPositionX.value = newValue.coerceIn(min, max)
                })
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clickable(
                            onClick = {
                                onNoteDeleteClick(note)
                            }
                        )
                )
                Image(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = "",
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .clickable(
                            onClick = {
                                onNoteEditClick(note)
                            }
                        )
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
                    .padding(4.dp, 2.dp, 4.dp, 2.dp)
                    .offset(offsetPositionX.value.dp, 0.dp)
                    .clickable(onClick = { onNoteClick(note) }), elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize(Alignment.CenterStart)
                        .padding(start = 8.dp)
                ) {
                    Text(text = note.title, style = MaterialTheme.typography.h6)
                    Spacer(Modifier.height(8.dp))
                    Text(text = formatDate(note.time), style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}

@Preview
@Composable
fun NoteItemPreview() {
    val note = Note(1, 1, "title", "description", "url", System.currentTimeMillis())
    NoteItem(note = note, onNoteClick = {}, onNoteDeleteClick = {}, onNoteEditClick = {})
}