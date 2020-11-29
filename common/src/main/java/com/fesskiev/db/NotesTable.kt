package com.fesskiev.db

import com.fesskiev.model.Note
import org.jetbrains.exposed.sql.*

object NotesTable : Table() {
    val noteUid = integer("noteUid").autoIncrement()
    val userUid = integer("userUid")
    val title = varchar("title", 255)
    val description = varchar("description", 255)
    val pictureUrl = varchar("pictureUrl", 255).nullable()
    val time = long("time")
    override val primaryKey = PrimaryKey(noteUid)
}

suspend fun insertNote(uidValue: Int, titleValue: String, descriptionValue: String, pictureUrlValue: String?): Note? = DatabaseFactory.dbQuery {
    val insertStatement = NotesTable.insert {
        it[userUid] = uidValue
        it[title] = titleValue
        it[description] = descriptionValue
        it[pictureUrl] = pictureUrlValue
        it[time] = System.currentTimeMillis()
    }
    val result = insertStatement.resultedValues?.get(0)
    if (result != null) {
        toNote(result)
    } else {
        null
    }
}

suspend fun selectNotes(uidValue: Int): List<Note> = DatabaseFactory.dbQuery {
    NotesTable.select {
        (NotesTable.userUid eq uidValue)
    }.map {
        toNote(it)
    }
}

suspend fun deleteNote(note: Note): Boolean = DatabaseFactory.dbQuery {
    NotesTable.deleteWhere {
        NotesTable.noteUid eq note.noteUid
    } > 0
}

suspend fun updateNote(note: Note): Boolean = DatabaseFactory.dbQuery {
    NotesTable.update({ NotesTable.noteUid eq note.noteUid }) {
        it[title] = note.title
        it[description] = note.description
        it[pictureUrl] = note.pictureUrl
    } > 0
}

private fun toNote(row: ResultRow): Note = Note(
    row[NotesTable.noteUid],
    row[NotesTable.userUid],
    row[NotesTable.title],
    row[NotesTable.description],
    row[NotesTable.pictureUrl],
    row[NotesTable.time],
)