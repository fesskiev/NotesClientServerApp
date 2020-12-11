package com.fesskiev.compose.data

import com.fesskiev.HTTPParameters.DISPLAY_NAME
import com.fesskiev.HTTPParameters.EMAIL
import com.fesskiev.HTTPParameters.NOTE_DESCRIPTION
import com.fesskiev.HTTPParameters.NOTE_PICTURE_URL
import com.fesskiev.HTTPParameters.NOTE_TITLE
import com.fesskiev.HTTPParameters.PASSWORD
import com.fesskiev.Routes.ADD_NOTE
import com.fesskiev.Routes.DELETE_NOTE
import com.fesskiev.Routes.GET_NOTES
import com.fesskiev.Routes.LOGIN
import com.fesskiev.Routes.LOGOUT
import com.fesskiev.Routes.REGISTRATION
import com.fesskiev.Routes.EDIT_NOTE
import com.fesskiev.model.JWTAuth
import com.fesskiev.model.Note
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(private val httpClient: HttpClient) : Repository {

    val notes: MutableList<Note> = mutableListOf()
    var isNotesCacheExpired: Boolean = true

    override suspend fun getNotes(): List<Note> = withContext(Dispatchers.IO) {
        if (isNotesCacheExpired) {
            val newNotes = httpClient.get<List<Note>>(GET_NOTES)
            notes.addAll(newNotes)
            isNotesCacheExpired = false
        }
        return@withContext notes
    }

    override suspend fun addNote(title: String, description: String, pictureUrl: String?): Note =
        withContext(Dispatchers.IO) {
            val newNote = httpClient.post<Note>(ADD_NOTE) {
                body = FormDataContent(Parameters.build {
                    append(NOTE_TITLE, title)
                    append(NOTE_DESCRIPTION, description)
                    pictureUrl?.let {
                        append(NOTE_PICTURE_URL, it)
                    }
                })
            }
            notes.add(newNote)
            return@withContext newNote
        }

    override suspend fun editNote(note: Note): Boolean = withContext(Dispatchers.IO) {
        val edited = httpClient.put<Boolean>(EDIT_NOTE) {
            body = defaultSerializer().write(note)
        }
        if (edited) {
            notes.forEachIndexed { index, it ->
                if (it.noteUid == note.noteUid) {
                    notes[index] = note
                }
            }
        }
        return@withContext edited
    }

    override suspend fun deleteNote(note: Note): Boolean = withContext(Dispatchers.IO) {
        val deleted = httpClient.delete<Boolean>(DELETE_NOTE) {
            body = defaultSerializer().write(note)
        }
        if (deleted) {
            notes.remove(note)
        }
        return@withContext deleted
    }

    override suspend fun registration(
        email: String,
        displayName: String,
        password: String
    ): JWTAuth = withContext(Dispatchers.IO) {
        httpClient.post(REGISTRATION) {
            body = FormDataContent(Parameters.build {
                append(EMAIL, email)
                append(DISPLAY_NAME, displayName)
                append(PASSWORD, password)
            })
        }
    }

    override suspend fun login(email: String, password: String): JWTAuth =
        withContext(Dispatchers.IO) {
            httpClient.post(LOGIN) {
                body = FormDataContent(Parameters.build {
                    append(EMAIL, email)
                    append(PASSWORD, password)
                })
            }
        }

    override suspend fun logout(): Unit = withContext(Dispatchers.IO) {
        httpClient.post(LOGOUT)
    }
}