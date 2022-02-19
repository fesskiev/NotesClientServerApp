package com.fesskiev.compose.data.remote

import com.fesskiev.HTTPParameters.DISPLAY_NAME
import com.fesskiev.HTTPParameters.EMAIL
import com.fesskiev.HTTPParameters.NOTE_DESCRIPTION
import com.fesskiev.HTTPParameters.NOTE_TITLE
import com.fesskiev.HTTPParameters.NOTE_UID
import com.fesskiev.HTTPParameters.PAGE
import com.fesskiev.HTTPParameters.PASSWORD
import com.fesskiev.Routes.ADD_NOTE
import com.fesskiev.Routes.ADD_NOTE_IMAGE
import com.fesskiev.Routes.DELETE_NOTE
import com.fesskiev.Routes.EDIT_NOTE
import com.fesskiev.Routes.GET_NOTES
import com.fesskiev.Routes.GET_USER
import com.fesskiev.Routes.LOGIN
import com.fesskiev.Routes.LOGOUT
import com.fesskiev.Routes.REGISTRATION
import com.fesskiev.compose.model.JWTAuth
import com.fesskiev.compose.model.Note
import com.fesskiev.compose.model.User
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.core.internal.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import java.io.File

class NetworkSourceImpl(private val httpClient: HttpClient) : NetworkSource {

    override suspend fun getUser(): User = withContext(IO) {
        return@withContext httpClient.get<User>(GET_USER)
    }

    override suspend fun pagingNotes(page: Int): List<Note> = withContext(IO) {
        val newNotes = httpClient.get<List<Note>>(GET_NOTES) {
            url.parameters.append(PAGE, page.toString())
        }
        return@withContext newNotes
    }

    override suspend fun addNote(title: String, description: String): Note =
        withContext(IO) {
            val newNote = httpClient.post<Note>(ADD_NOTE) {
                body = FormDataContent(Parameters.build {
                    append(NOTE_TITLE, title)
                    append(NOTE_DESCRIPTION, description)
                })
            }
            return@withContext newNote
        }

    @OptIn(DangerousInternalIoApi::class)
    override suspend fun addImage(note: Note, file: File): Note =
        withContext(IO) {
            val formData = formData {
                val contentType = ContentType.MultiPart.FormData
                append("image", InputProvider(file.length()) { file.inputStream().asInput() },
                    Headers.build {
                        append(HttpHeaders.ContentType, contentType.toString())
                        append(HttpHeaders.ContentDisposition, "filename=${file.name}")
                    }
                )
            }
            val newNote = httpClient.submitFormWithBinaryData<Note>(ADD_NOTE_IMAGE, formData) {
                parameter(NOTE_UID, note.noteUid)
            }
            return@withContext newNote
        }

    override suspend fun editNote(note: Note): Boolean =
        withContext(IO) {
            val edited = httpClient.put<Boolean>(EDIT_NOTE) {
                body = defaultSerializer().write(note)
            }
            return@withContext edited
        }

    override suspend fun deleteNote(note: Note): Boolean = withContext(IO) {
        val deleted = httpClient.delete<Boolean>(DELETE_NOTE) {
            body = defaultSerializer().write(note)
        }
        return@withContext deleted
    }

    override suspend fun registration(
        email: String,
        displayName: String,
        password: String
    ): JWTAuth = withContext(IO) {
        httpClient.post(REGISTRATION) {
            body = FormDataContent(Parameters.build {
                append(EMAIL, email)
                append(DISPLAY_NAME, displayName)
                append(PASSWORD, password)
            })
        }
    }

    override suspend fun login(email: String, password: String): JWTAuth =
        withContext(IO) {
            httpClient.post(LOGIN) {
                body = FormDataContent(Parameters.build {
                    append(EMAIL, email)
                    append(PASSWORD, password)
                })
            }
        }

    override suspend fun logout(): Unit = withContext(IO) {
        httpClient.post<Unit>(LOGOUT)
    }
}