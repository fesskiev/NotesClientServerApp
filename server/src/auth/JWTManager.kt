package com.fesskiev.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.fesskiev.model.JWTAuth
import java.util.*

class JWTManager {

    private val jwtSecret = "108467332"
    private val algorithm = Algorithm.HMAC512(jwtSecret)
    val issuer = "NotesServer"

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    fun generateToken(uid: String): JWTAuth = JWTAuth(JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("uid", uid)
        .withExpiresAt(expiresAt())
        .sign(algorithm))

    private fun expiresAt() = Date(System.currentTimeMillis() + 3600000 * 24) // 24 hours
}
