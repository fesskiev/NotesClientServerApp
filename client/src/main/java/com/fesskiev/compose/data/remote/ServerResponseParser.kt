package com.fesskiev.compose.data.remote

import com.fesskiev.model.JWTAuth
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

fun parseJWT(body: String): JWTAuth? = try {
    Moshi.Builder().add(KotlinJsonAdapterFactory()).build().adapter(JWTAuth::class.java)
        .fromJson(body)
} catch (e: Exception) {
    e.printStackTrace()
    null
}