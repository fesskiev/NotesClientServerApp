package com.fesskiev.compose.data

import com.fesskiev.HTTPParameters.DISPLAY_NAME
import com.fesskiev.HTTPParameters.EMAIL
import com.fesskiev.HTTPParameters.NOTE_TEXT
import com.fesskiev.HTTPParameters.PASSWORD
import com.fesskiev.Routes.ADD_NOTE
import com.fesskiev.Routes.DELETE_NOTE
import com.fesskiev.Routes.GET_NOTES
import com.fesskiev.Routes.LOGIN
import com.fesskiev.Routes.LOGOUT
import com.fesskiev.Routes.REGISTRATION
import com.fesskiev.Routes.UPDATE_NOTE
import com.fesskiev.model.JWTAuth
import com.fesskiev.model.Note
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryImpl(private val httpClient: HttpClient) : Repository {

    override suspend fun getNotes(): List<Note> = withContext(Dispatchers.IO) {
         httpClient.get(GET_NOTES)
    }

    override suspend fun addNote(text: String): Note = withContext(Dispatchers.IO) {
        httpClient.post(ADD_NOTE) {
            body = FormDataContent(Parameters.build {
                append(NOTE_TEXT, text)
            })
        }
    }

    override suspend fun updateNote(note: Note): Boolean = withContext(Dispatchers.IO) {
        httpClient.put(UPDATE_NOTE) {
            body = defaultSerializer().write(note)
        }
    }

    override suspend fun deleteNote(note: Note): Boolean = withContext(Dispatchers.IO) {
        httpClient.delete(DELETE_NOTE) {
            body = defaultSerializer().write(note)
        }
    }

    override suspend fun registration(email: String, displayName: String, password: String): JWTAuth = withContext(Dispatchers.IO) {
        httpClient.post(REGISTRATION) {
            body = FormDataContent(Parameters.build {
                append(EMAIL, email)
                append(DISPLAY_NAME, displayName)
                append(PASSWORD, password)
            })
        }
    }

    override suspend fun login(email: String, password: String): JWTAuth = withContext(Dispatchers.IO) {
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