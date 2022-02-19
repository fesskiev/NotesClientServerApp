package com.fesskiev.routes

import com.fesskiev.HTTPParameters.DISPLAY_NAME
import com.fesskiev.HTTPParameters.EMAIL
import com.fesskiev.HTTPParameters.PASSWORD
import com.fesskiev.Routes.GET_USER
import com.fesskiev.Routes.LOGIN
import com.fesskiev.Routes.LOGOUT
import com.fesskiev.Routes.REGISTRATION
import com.fesskiev.ServerErrorCodes.DISPLAY_NAME_EMPTY
import com.fesskiev.ServerErrorCodes.EMAIL_ALREADY_USE
import com.fesskiev.ServerErrorCodes.EMAIL_EMPTY
import com.fesskiev.ServerErrorCodes.PASSWORD_EMPTY
import com.fesskiev.ServerErrorCodes.PASSWORD_INCORRECT
import com.fesskiev.ServerErrorCodes.USER_NOT_CREATED
import com.fesskiev.ServerErrorCodes.USER_NOT_FOUND
import com.fesskiev.auth.JWTManager
import com.fesskiev.auth.UserSession
import com.fesskiev.model.ServerError
import com.fesskiev.repository.Repository
import io.ktor.application.*
import io.ktor.auth.*
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
        val email = parameters[EMAIL] ?: return@post call.respond(Unauthorized, ServerError(EMAIL_EMPTY))
        val displayName = parameters[DISPLAY_NAME] ?: return@post call.respond(Unauthorized, ServerError(DISPLAY_NAME_EMPTY))
        val password = parameters[PASSWORD] ?: return@post call.respond(Unauthorized, ServerError(PASSWORD_EMPTY))
        repository.getUserByEmail(email)?.let {
            call.respond(Unauthorized, ServerError(EMAIL_ALREADY_USE))
        }
        val user = repository.createUser(email, displayName, password)
        if (user == null) {
            call.respond(Unauthorized, ServerError(USER_NOT_CREATED))
        } else {
            call.sessions.set(UserSession(user.uid))
            call.respond(Created, jwtManager.generateToken(user.uid))
        }
    }

    post(LOGIN) {
        val parameters = call.receive<Parameters>()
        val email = parameters[EMAIL] ?: return@post call.respond(Unauthorized, ServerError(EMAIL_EMPTY))
        val password = parameters[PASSWORD] ?: return@post call.respond(Unauthorized, ServerError(PASSWORD_EMPTY))
        val user = repository.getUserByEmail(email)
        if (user == null) {
            call.respond(BadRequest, ServerError(USER_NOT_FOUND))
        } else {
            if (user.password != password) {
                call.respond(BadRequest, ServerError(PASSWORD_INCORRECT))
            } else {
                call.sessions.set(UserSession(user.uid))
                call.respond(OK, jwtManager.generateToken(user.uid))
            }
        }
    }

    authenticate {
        post(LOGOUT) {
            call.sessions.get<UserSession>()?.let { session ->
                val user = repository.getUserByUid(session.userUid)
                if (user == null) {
                    call.respond(BadRequest, ServerError(USER_NOT_FOUND))
                    return@post
                } else {
                    call.sessions.clear(call.sessions.findName(UserSession::class))
                    call.respond(OK)
                }
            }
        }

        get(GET_USER) {
            call.sessions.get<UserSession>()?.let { session ->
                val user = repository.getUserByUid(session.userUid)
                if (user == null) {
                    call.respond(BadRequest, ServerError(USER_NOT_FOUND))
                    return@get
                } else {
                    call.respond(OK, user)
                }
            }
        }
    }
}