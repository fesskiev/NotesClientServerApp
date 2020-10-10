package com.fesskiev

import com.fesskiev.auth.JWTManager
import com.fesskiev.auth.UserSession
import com.fesskiev.repository.RepositoryImpl
import com.fesskiev.routes.notes
import com.fesskiev.routes.users
import io.ktor.application.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.server.netty.*
import java.text.DateFormat

/**
 * https://ktor.io/docs/quickstart-index.html
 * https://www.raywenderlich.com/7265034-ktor-rest-api-for-mobile
 */
fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {

    install(Sessions) {
        header<UserSession>("Session")
    }

    val jwtManager = JWTManager()
    val repository = RepositoryImpl()

    install(Authentication) {
        jwt{
            verifier(jwtManager.verifier)
            realm = jwtManager.issuer
            validate {
                val payload = it.payload
                val uid = payload.getClaim("uid").asString()
                val user = repository.getUserByUid(uid)
                user
            }
        }
    }

    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    routing {
        notes(repository)
        users(repository, jwtManager)
    }
}

