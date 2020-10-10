package com.fesskiev.compose.data.remote

import com.fesskiev.model.JWTAuth
import kotlinx.serialization.json.Json

fun parseJWT(body: String): JWTAuth? = try {
    Json.decodeFromString(JWTAuth.serializer(), body)
} catch (e: Exception) {
    e.printStackTrace()
    null
}