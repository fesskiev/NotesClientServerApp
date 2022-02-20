package com.fesskiev.compose.data

import com.fesskiev.compose.model.JWTAuth
import com.fesskiev.compose.model.Note
import java.io.File

interface Repository {

    suspend fun login(email: String, password: String): JWTAuth

    suspend fun registration(email: String, displayName: String, password: String): JWTAuth

    suspend fun pagingNotes(page: Int): PagingResult<Note>

    suspend fun addNote(title: String, description: String, file: File?): Note

    suspend fun editNote(note: Note): Boolean

    suspend fun deleteNote(note: Note): Boolean

    suspend fun deleteAllNotes()

    suspend fun logout()

    suspend fun searchNotes(query: String): List<Note>
}