package com.fesskiev.compose.data

import com.fesskiev.model.JWTAuth
import com.fesskiev.model.Note

interface Repository  {

    suspend fun getNotes(): List<Note>

    suspend fun addNote(title: String, description: String, pictureUrl: String?): Note

    suspend fun editNote(note: Note): Boolean

    suspend fun deleteNote(note: Note): Boolean

    suspend fun registration(email: String, displayName: String, password: String): JWTAuth

    suspend fun login(email: String, password: String): JWTAuth

    suspend fun logout()
}