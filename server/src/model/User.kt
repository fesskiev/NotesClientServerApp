package com.fesskiev.model

import io.ktor.auth.*

import kotlinx.serialization.Serializable

@Serializable
data class User(val uid: Int, val email: String, val displayName: String, val password: String) : Principal