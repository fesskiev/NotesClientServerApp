package com.fesskiev.compose.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.*
import androidx.compose.runtime.*
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
import com.fesskiev.model.Note

@OptIn(ExperimentalFoundationApi::class)
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
    LazyVerticalGrid(
        cells = GridCells.Adaptive(minSize = 164.dp)
    ) {
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

@OptIn(ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class)
@Composable
fun NoteItem(
    note: Note,
    onNoteClick: (Note) -> Unit,
    onNoteDeleteClick: (Note) -> Unit,
    onNoteEditClick: (Note) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .combinedClickable(
                onClick = { onNoteClick(note)},
                onLongClick = { expanded = !expanded }
            ),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = note.title, style = MaterialTheme.typography.subtitle1)
            Spacer(Modifier.height(8.dp))
            Text(text = note.description, style = MaterialTheme.typography.body2)
            Spacer(Modifier.height(8.dp))
            AnimatedVisibility(visible = expanded) {
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
                                    onNoteEditClick(note)
                                }
                            )
                    )
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