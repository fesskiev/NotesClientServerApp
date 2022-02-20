package com.fesskiev.compose.data.local

import com.fesskiev.compose.model.Note
import com.fesskiev.compose.model.User

interface DatabaseSource {

    suspend fun saveUser(user: User)

    suspend fun pagingNotes(uid: Long, page: Int): List<Note>

    suspend fun saveNotes(notes: List<Note>)

    suspend fun editNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun deleteAllNotes()

    suspend fun searchNotes(query: String): List<Note>
}