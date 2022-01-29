package com.fesskiev.compose.data

import com.fesskiev.model.JWTAuth
import com.fesskiev.model.Note
import java.io.File

interface Repository {

    suspend fun getNotes(page: Int): List<Note>

    suspend fun getNoteById(noteUid: Int): Note

    suspend fun addNote(title: String, description: String): Note

    suspend fun editNote(note: Note): Boolean

    suspend fun deleteNote(note: Note): Boolean

    suspend fun registration(email: String, displayName: String, password: String): JWTAuth

    suspend fun login(email: String, password: String): JWTAuth

    suspend fun logout()

    suspend fun addImage(note: Note, file: File): Note
}