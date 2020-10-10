package com.fesskiev.compose.data

import com.fesskiev.model.JWTAuth
import com.fesskiev.model.Note

interface Repository  {

    suspend fun getNotes(): List<Note>

    suspend fun addNote(text: String): Note

    suspend fun updateNote(note: Note): Boolean

    suspend fun deleteNote(note: Note): Boolean

    suspend fun registration(email: String, displayName: String, password: String): JWTAuth

    suspend fun login(email: String, password: String): JWTAuth

    suspend fun logout()
}