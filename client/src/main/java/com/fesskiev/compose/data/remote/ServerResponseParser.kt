package com.fesskiev.compose.data.remote

import com.fesskiev.model.JWTAuth
import com.fesskiev.model.ServerError
import kotlinx.serialization.json.Json

fun parseJWT(body: String): JWTAuth? = try {
    Json.decodeFromString(JWTAuth.serializer(), body)
} catch (e: Exception) {
    null
}

fun parseServerError(body: String): ServerError? = try {
    Json.decodeFromString(ServerError.serializer(), body)
} catch (e: Exception) {
    null
}