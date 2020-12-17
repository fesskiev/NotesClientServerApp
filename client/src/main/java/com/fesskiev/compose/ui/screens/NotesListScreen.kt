package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R
import com.fesskiev.compose.ui.utils.formatDate
import com.fesskiev.model.Note

@Composable
fun NotesListScreen(
    notes: List<Note>, noteOnClick: (Note) -> Unit,
    deleteNoteOnClick: (Note) -> Unit,
    editNoteOnClick: (Note) -> Unit
) {
    LazyColumnFor(notes) { note ->
        NoteItem(note, noteOnClick, deleteNoteOnClick, editNoteOnClick)
    }
}

@Composable
fun NoteItem(
    note: Note,
    noteOnClick: (Note) -> Unit,
    deleteNoteOnClick: (Note) -> Unit,
    editNoteOnClick: (Note) -> Unit
) {
    WithConstraints {
        val max = 0.dp
        val min = (-150).dp
        val offsetPositionX = remember { mutableStateOf(0f) }
        val offsetPositionY = remember { mutableStateOf(0f) }
        Box(
            Modifier.draggable(orientation = Orientation.Horizontal) { delta ->
                val newValue = offsetPositionX.value + delta
                offsetPositionX.value = newValue.coerceIn(min.toPx(), max.toPx())
            }
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().height(72.dp)
            ) {
                Image(
                    imageVector = vectorResource(R.drawable.ic_delete),
                    modifier = Modifier.padding(horizontal = 20.dp).clickable(
                        onClick = {
                            deleteNoteOnClick(note)
                        }
                    )
                )
                Image(
                    imageVector = vectorResource(R.drawable.ic_edit),
                    modifier = Modifier.padding(horizontal = 20.dp).clickable(
                        onClick = {
                            editNoteOnClick(note)
                        }
                    )
                )
            }
            Card(
                modifier = Modifier.fillMaxWidth().height(72.dp).padding(4.dp, 2.dp, 4.dp, 2.dp)
                    .offset({ offsetPositionX.value }, { offsetPositionY.value })
                    .clickable(onClick = { noteOnClick(note) }), elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.wrapContentSize(Alignment.CenterStart).padding(start = 8.dp)
                ) {
                    Text(
                        note.title,
                        style = TextStyle(color = Color(0xFF272729), fontWeight = FontWeight.Bold)
                    )
                    Text(
                        formatDate(note.time),
                        style = TextStyle(color = Color(0xFF272729), fontWeight = FontWeight.Normal)
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
    NoteItem(note = note, noteOnClick = {}, deleteNoteOnClick = {}, editNoteOnClick = {})
}