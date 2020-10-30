package com.fesskiev

import com.fesskiev.Database.SERVER_URL
import com.fesskiev.Headers.SESSION
import com.fesskiev.auth.JWTManager
import com.fesskiev.auth.UserSession
import com.fesskiev.db.DatabaseFactory
import com.fesskiev.model.ServerError
import com.fesskiev.repository.RepositoryImpl
import com.fesskiev.routes.notes
import com.fesskiev.routes.users
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import io.ktor.server.netty.*
import io.ktor.sessions.*
import kotlinx.serialization.json.Json

/**
 * https://ktor.io/docs/quickstart-index.html
 * https://www.raywenderlich.com/7265034-ktor-rest-api-for-mobile
 * https://github.com/JetBrains/kotlinconf-app
 */
fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {

    install(Sessions) {
        header<UserSession>(SESSION)
    }

    DatabaseFactory.init(url = SERVER_URL)
    val jwtManager = JWTManager()
    val repository = RepositoryImpl()

    install(StatusPages) {
        exception<Throwable> { e ->
            call.respond(InternalServerError, ServerError(e.message ?: ""))
        }
    }
    install(ContentNegotiation) {
        json(
            json = Json,
            contentType = ContentType.Application.Json
        )
    }

    install(Authentication) {
        jwt {
            verifier(jwtManager.verifier)
            realm = jwtManager.issuer
            validate {
                val payload = it.payload
                val uid = payload.getClaim("uid").asInt()
                val user = repository.getUserByUid(uid)
                user
            }
        }
    }
    routing {
        notes(repository)
        users(repository, jwtManager)
    }
}

