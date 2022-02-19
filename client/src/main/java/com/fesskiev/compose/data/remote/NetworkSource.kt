package com.fesskiev.compose.data.remote

import com.fesskiev.compose.model.JWTAuth
import com.fesskiev.compose.model.Note
import com.fesskiev.compose.model.User
import java.io.File

interface NetworkSource {

    suspend fun pagingNotes(page: Int): List<Note>

    suspend fun addNote(title: String, description: String): Note

    suspend fun editNote(note: Note): Boolean

    suspend fun deleteNote(note: Note): Boolean

    suspend fun registration(email: String, displayName: String, password: String): JWTAuth

    suspend fun login(email: String, password: String): JWTAuth

    suspend fun logout()

    suspend fun addImage(note: Note, file: File): Note

    suspend fun getUser(): User
}