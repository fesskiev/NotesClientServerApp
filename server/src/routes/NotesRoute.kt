package com.fesskiev.routes

import com.fesskiev.HTTPParameters.NOTE_DESCRIPTION
import com.fesskiev.HTTPParameters.NOTE_PICTURE_URL
import com.fesskiev.HTTPParameters.NOTE_TITLE
import com.fesskiev.Routes.ADD_NOTE
import com.fesskiev.Routes.DELETE_NOTE
import com.fesskiev.Routes.GET_NOTES
import com.fesskiev.Routes.EDIT_NOTE
import com.fesskiev.ServerErrorCodes.NOTE_DESCRIPTION_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_NOT_ADDED
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
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

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
                    val notes = repository.getNotes(user.uid)
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
                    val title = parameters[NOTE_TITLE] ?: return@post call.respond(BadRequest, ServerError(NOTE_TITLE_EMPTY))
                    val description = parameters[NOTE_DESCRIPTION] ?: return@post call.respond(BadRequest, ServerError(NOTE_DESCRIPTION_EMPTY))
                    val pictureUrl = parameters[NOTE_PICTURE_URL]
                    val note = repository.addNote(user.uid, title, description, pictureUrl)
                    if (note == null) {
                        call.respond(BadRequest, ServerError(NOTE_NOT_ADDED))
                    } else {
                        call.respond(Created, note)
                    }

                }
            }
        }

        put(EDIT_NOTE) {
            val note = call.receiveOrNull<Note>() ?: return@put call.respond(BadRequest, ServerError(NOTE_EMPTY))
            val result = repository.editNote(note)
            call.respond(OK, result)
        }

        delete(DELETE_NOTE) {
            val note = call.receiveOrNull<Note>() ?: return@delete call.respond(BadRequest, ServerError(NOTE_EMPTY))
            val result = repository.removeNote(note)
            call.respond(OK, result)
        }
    }
}