package com.fesskiev.compose.ui.screens.notes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.fesskiev.model.Note
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun NotesList(notes: List<Note>, noteOnClick: (Note) -> Unit) {
    LazyColumnFor(notes) { note ->
        NoteItem(note, noteOnClick)
    }
}

@Composable
fun NoteItem(note: Note, noteOnClick: (Note) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(72.dp).padding(4.dp, 2.dp, 4.dp, 2.dp)
            .clickable(onClick = { noteOnClick(note) }), elevation = 4.dp
    ) {
        Column(modifier = Modifier.wrapContentSize(Alignment.CenterStart).padding(4.dp)) {
            Text(
                note.text,
                style = TextStyle(color = Color(0xFF272729), fontWeight = FontWeight.Bold)
            )
            val formatter = SimpleDateFormat("dd.MM.yyyy")
            val formattedDate = formatter.format(Date(note.time))
            Text(
                formattedDate,
                style = TextStyle(color = Color(0xFF272729), fontWeight = FontWeight.Normal)
            )
        }
    }
}

@Preview
@Composable
fun NoteItemPreview() {
    val note = Note(1, 1, "test", System.currentTimeMillis())
    NoteItem(note = note, noteOnClick = {

    })
}