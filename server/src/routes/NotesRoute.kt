package com.fesskiev.routes

import com.fesskiev.HTTPParameters.NOTE_TEXT
import com.fesskiev.Routes.ADD_NOTE
import com.fesskiev.Routes.DELETE_NOTE
import com.fesskiev.Routes.GET_NOTES
import com.fesskiev.Routes.UPDATE_NOTE
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
            if (session != null) {
                val user = repository.getUserByUid(session.userId)
                if (user == null) {
                    call.respond(BadRequest, ServerError("Problems retrieving User"))
                    return@get
                } else {
                    val notes = repository.getNotes(user.uid)
                    call.respond(OK, notes)
                }
            } else {
                call.respond(BadRequest, ServerError("Problems retrieving session"))
            }
        }

        post(ADD_NOTE) {
            val session = call.sessions.get<UserSession>()
            if (session != null) {
                val user = repository.getUserByUid(session.userId)
                if (user == null) {
                    call.respond(BadRequest, ServerError("Problems retrieving User"))
                    return@post
                } else {
                    val parameters: Parameters = call.receiveParameters()
                    val text = parameters[NOTE_TEXT] ?: return@post call.respond(BadRequest, ServerError("Missing text of note"))
                    val note = repository.addNote(user.uid, text)
                    call.respond(Created, note)
                }
            } else {
                call.respond(BadRequest, ServerError("Problems retrieving session"))
            }
        }

        put(UPDATE_NOTE) {
            val note = call.receiveOrNull<Note>() ?: return@put call.respond(BadRequest, ServerError("Missing note"))
            val result = repository.updateNote(note)
            call.respond(OK, result)
        }

        delete(DELETE_NOTE) {
            val note = call.receiveOrNull<Note>() ?: return@delete call.respond(BadRequest, ServerError("Missing note"))
            val result = repository.deleteNote(note)
            call.respond(OK, result)
        }
    }
}