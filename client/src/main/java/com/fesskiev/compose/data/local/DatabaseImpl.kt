package com.fesskiev.compose.data.local

import com.fesskiev.compose.SqlDelight
import com.fesskiev.compose.model.Note
import com.fesskiev.compose.model.User
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext

class DatabaseImpl(private val sqlDelight: SqlDelight) : DatabaseSource {

    override suspend fun saveUser(user: User) = withContext(IO) {
        sqlDelight.db_schemaQueries.insertUser(
            uid = user.uid,
            email = user.email,
            displayName = user.displayName,
            password = user.password
        )
    }

    override suspend fun searchNotes(query: String): List<Note> = withContext(IO) {
        return@withContext sqlDelight.db_schemaQueries.searchNotes(query) { noteUid, userUid, title, description, pictureName, time ->
            Note(
                noteUid = noteUid,
                userUid = userUid,
                title = title,
                description = description,
                pictureName = pictureName,
                time = time
            )
        }.executeAsList()
    }

    override suspend fun pagingNotes(uid: Long, page: Int): List<Note> = withContext(IO) {
        val limit = 10L
        val offset = ((page - 1) * 10).toLong()
        return@withContext sqlDelight.db_schemaQueries.selectNotesByUserUid(
            userUid = uid,
            limit = limit,
            offset = offset
        ) { noteUid, userUid, title, description, pictureName, time ->
            Note(
                noteUid = noteUid,
                userUid = userUid,
                title = title,
                description = description,
                pictureName = pictureName,
                time = time
            )
        }.executeAsList()
    }

    override suspend fun saveNotes(notes: List<Note>) = withContext(IO) {
        with(sqlDelight.db_schemaQueries) {
            transaction {
                notes.forEach { note ->
                    insertNote(
                        noteUid = note.noteUid,
                        userUid = note.userUid,
                        title = note.title,
                        description = note.description,
                        pictureName = note.pictureName,
                        time = note.time
                    )
                }
            }
        }
    }

    override suspend fun editNote(note: Note) = withContext(IO) {
        sqlDelight.db_schemaQueries.updateNoteByUid(
            title = note.title,
            description = note.description,
            noteUid = note.noteUid
        )
    }

    override suspend fun deleteNote(note: Note) = withContext(IO) {
        sqlDelight.db_schemaQueries.deleteLanguageByUid(note.noteUid)
    }

    override suspend fun deleteAllNotes() = withContext(IO) {
        sqlDelight.db_schemaQueries.deleteAllNotes()
    }
}
