package com.fesskiev.db

import com.fesskiev.model.Note
import org.jetbrains.exposed.sql.*

object NotesTable : Table() {
    val noteUid = integer("noteUid").autoIncrement()
    val userUid = integer("userUid")
    val title = text("title")
    val description = text("description", )
    val pictureName = varchar("pictureName", 255).nullable()
    val time = long("time")
    override val primaryKey = PrimaryKey(noteUid)
}

suspend fun insertNote(uidValue: Int, titleValue: String, descriptionValue: String): Note? = DatabaseFactory.dbQuery {
    val insertStatement = NotesTable.insert {
        it[userUid] = uidValue
        it[title] = titleValue
        it[description] = descriptionValue
        it[time] = System.currentTimeMillis()
    }
    val result = insertStatement.resultedValues?.get(0)
    if (result != null) {
        toNote(result)
    } else {
        null
    }
}

suspend fun selectNotes(uidValue: Int, page: Int): List<Note> = DatabaseFactory.dbQuery {
    val limit = 10
    val offset = (page - 1) * 10
    NotesTable.select { (NotesTable.userUid eq uidValue) }
        .orderBy(NotesTable.time to SortOrder.DESC)
        .limit(limit, offset.toLong())
        .map { toNote(it) }
}

suspend fun selectNoteById(uidValue: Int): Note? = DatabaseFactory.dbQuery {
    NotesTable.select {(NotesTable.noteUid eq uidValue) }
        .map { toNote(it) }
        .firstOrNull()
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
        it[pictureName] = note.pictureName
    } > 0
}

private fun toNote(row: ResultRow): Note = Note(
    row[NotesTable.noteUid],
    row[NotesTable.userUid],
    row[NotesTable.title],
    row[NotesTable.description],
    row[NotesTable.pictureName],
    row[NotesTable.time],
)