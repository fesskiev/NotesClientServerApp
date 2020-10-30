package com.fesskiev.routes

import com.fesskiev.HTTPParameters.DISPLAY_NAME
import com.fesskiev.HTTPParameters.EMAIL
import com.fesskiev.HTTPParameters.PASSWORD
import com.fesskiev.Routes.LOGIN
import com.fesskiev.Routes.LOGOUT
import com.fesskiev.Routes.REGISTRATION
import com.fesskiev.auth.JWTManager
import com.fesskiev.auth.UserSession
import com.fesskiev.model.ServerError
import com.fesskiev.repository.Repository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.Created
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.Unauthorized
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*

fun Route.users(repository: Repository, jwtManager: JWTManager) {

    post(REGISTRATION) {
        val parameters = call.receive<Parameters>()
        val email = parameters[EMAIL] ?: return@post call.respond(Unauthorized, ServerError("Missing email"))
        val displayName = parameters[DISPLAY_NAME] ?: return@post call.respond(Unauthorized, ServerError("Missing display name"))
        val password = parameters[PASSWORD] ?: return@post call.respond(Unauthorized, ServerError("Missing password"))
        repository.getUserByEmail(email)?.let {
            call.respond(Unauthorized, ServerError("email $email already use"))
        }
        val user = repository.createUser(email, displayName, password)
        if (user == null) {
            call.respond(Unauthorized, ServerError("Problems creating User"))
        } else {
            call.sessions.set(UserSession(user.uid))
            call.respond(Created, jwtManager.generateToken(user.uid))
        }
    }

    post(LOGIN) {
        val parameters = call.receive<Parameters>()
        val email = parameters[EMAIL] ?: return@post call.respond(Unauthorized, ServerError("Missing email"))
        val password = parameters[PASSWORD] ?: return@post call.respond(Unauthorized, ServerError("Missing password"))
        val user = repository.getUserByEmail(email)
        if (user == null) {
            call.respond(BadRequest, ServerError("Missing user with email: $email"))
        } else {
            if (user.password != password) {
                call.respond(BadRequest, ServerError("Password incorrect"))
            } else {
                call.sessions.set(UserSession(user.uid))
                call.respond(OK, jwtManager.generateToken(user.uid))
            }
        }
    }

    post(LOGOUT) {
        call.sessions.get<UserSession>()?.let { session ->
            val user = repository.getUserByUid(session.userUid)
            if (user == null) {
                call.respond(BadRequest, ServerError("Problems retrieving User"))
                return@post
            } else {
                call.sessions.clear(call.sessions.findName(UserSession::class))
                call.respond(OK)
            }
        }
    }
}