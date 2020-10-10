package com.fesskiev.compose.data

import com.fesskiev.compose.data.remote.ApiService
import com.fesskiev.model.JWTAuth
import com.fesskiev.model.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(private val apiService: ApiService) : Repository {

    override suspend fun getNotes(): List<Note> = withContext(Dispatchers.IO) {
        apiService.getNotes()
    }

    override suspend fun addNote(text: String): Note = withContext(Dispatchers.IO) {
        apiService.addNote(text)
    }

    override suspend fun updateNote(note: Note): Boolean = withContext(Dispatchers.IO) {
        apiService.updateNote(note)
    }

    override suspend fun deleteNote(note: Note): Boolean = withContext(Dispatchers.IO) {
        apiService.deleteNote(note)
    }

    override suspend fun registration(email: String, displayName: String, password: String): JWTAuth = withContext(Dispatchers.IO) {
        apiService.registration(email, displayName, password)
    }

    override suspend fun login(email: String, password: String): JWTAuth = withContext(Dispatchers.IO) {
        apiService.login(email, password)
    }

    override suspend fun logout() = withContext(Dispatchers.IO) {
       apiService.logout()
    }
}