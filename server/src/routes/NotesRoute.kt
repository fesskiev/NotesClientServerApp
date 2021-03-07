package com.fesskiev.routes

import com.fesskiev.HTTPParameters.NOTE_DESCRIPTION
import com.fesskiev.HTTPParameters.NOTE_TITLE
import com.fesskiev.HTTPParameters.NOTE_UID
import com.fesskiev.HTTPParameters.PAGE
import com.fesskiev.IMAGES_DIR_PATH
import com.fesskiev.Routes.ADD_NOTE
import com.fesskiev.Routes.ADD_NOTE_IMAGE
import com.fesskiev.Routes.DELETE_NOTE
import com.fesskiev.Routes.EDIT_NOTE
import com.fesskiev.Routes.GET_NOTES
import com.fesskiev.ServerErrorCodes.IMAGE_NOT_ADDED
import com.fesskiev.ServerErrorCodes.NOTE_DESCRIPTION_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_ID_NOT_FOUND
import com.fesskiev.ServerErrorCodes.NOTE_NOT_ADDED
import com.fesskiev.ServerErrorCodes.NOTE_NOT_FOUND
import com.fesskiev.ServerErrorCodes.NOTE_TITLE_EMPTY
import com.fesskiev.ServerErrorCodes.SESSION_NOT_FOUND
import com.fesskiev.ServerErrorCodes.USER_NOT_FOUND
import com.fesskiev.auth.UserSession
import com.fesskiev.model.Note
import com.fesskiev.model.ServerError
import com.fesskiev.repository.Repository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import java.io.File

fun Route.notes(repository: Repository) {

    authenticate {
        get(GET_NOTES) {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respond(BadRequest, ServerError(SESSION_NOT_FOUND))
            } else {
                val user = repository.getUserByUid(session.userUid)
                if (user == null) {
                    call.respond(BadRequest, ServerError(USER_NOT_FOUND))
                    return@get
                } else {
                    val parameters: Parameters = call.parameters
                    val page = parameters[PAGE]?.toInt() ?: 1

                    val notes = repository.getNotes(user.uid, page)
                    call.respond(OK, notes)
                }
            }
        }

        post(ADD_NOTE) {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respond(BadRequest, ServerError(SESSION_NOT_FOUND))
            } else {
                val user = repository.getUserByUid(session.userUid)
                if (user == null) {
                    call.respond(BadRequest, ServerError(USER_NOT_FOUND))
                    return@post
                } else {
                    val parameters: Parameters = call.receiveParameters()
                    val title = parameters[NOTE_TITLE] ?: return@post call.respond(
                        BadRequest,
                        ServerError(NOTE_TITLE_EMPTY)
                    )
                    val description = parameters[NOTE_DESCRIPTION] ?: return@post call.respond(
                        BadRequest,
                        ServerError(NOTE_DESCRIPTION_EMPTY)
                    )
                    val note = repository.addNote(user.uid, title, description)
                    if (note == null) {
                        call.respond(BadRequest, ServerError(NOTE_NOT_ADDED))
                    } else {
                        call.respond(Created, note)
                    }
                }
            }
        }

        post(ADD_NOTE_IMAGE) {
            val session = call.sessions.get<UserSession>()
            if (session == null) {
                call.respond(BadRequest, ServerError(SESSION_NOT_FOUND))
            } else {
                val user = repository.getUserByUid(session.userUid)
                if (user == null) {
                    call.respond(BadRequest, ServerError(USER_NOT_FOUND))
                    return@post
                } else {
                    val parameters: Parameters = call.parameters
                    val noteUid = parameters[NOTE_UID]?.toInt()
                    if (noteUid == null) {
                        call.respond(BadRequest, ServerError(NOTE_ID_NOT_FOUND))
                        return@post
                    }
                    var filename = ""
                    val multipart = call.receiveMultipart()
                    multipart.forEachPart { part ->
                        if (part is PartData.FileItem) {
                            filename = "${System.currentTimeMillis()}-${part.originalFileName}"
                            part.streamProvider().use { its ->
                                getImageFilePath(filename).outputStream().buffered()
                                    .use { its.copyTo(it) }
                            }
                        }
                        part.dispose()
                    }
                    if (filename.isEmpty()) {
                        call.respond(BadRequest, ServerError(IMAGE_NOT_ADDED))
                    } else {
                        val note = repository.getNoteByUid(noteUid)
                        if (note == null) {
                            call.respond(BadRequest, ServerError(NOTE_NOT_FOUND))
                            return@post
                        }
                        val newNote = note.copy(pictureName = filename)
                        val result = repository.editNote(newNote)
                        if (result) {
                            call.respond(Created, newNote)
                        }
                    }
                }
            }
        }

        put(EDIT_NOTE) {
            val note = call.receiveOrNull<Note>() ?: return@put call.respond(
                BadRequest,
                ServerError(NOTE_EMPTY)
            )
            val result = repository.editNote(note)
            call.respond(OK, result)
        }

        delete(DELETE_NOTE) {
            val note = call.receiveOrNull<Note>() ?: return@delete call.respond(
                BadRequest,
                ServerError(NOTE_EMPTY)
            )
            val result = repository.removeNote(note)
            call.respond(OK, result)
        }
    }

    get("/{filename}") {
        val name = call.parameters["filename"]
        if (name == null) {
            call.respond(NotFound)
        } else {
            val file = getImageFilePath(name)
            if (file.exists()) {
                call.respondFile(file)
            } else {
                call.respond(NotFound)
            }
        }
    }
}

private fun getImageFilePath(filename: String) = File(IMAGES_DIR_PATH + filename)
