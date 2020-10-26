package com.fesskiev.model

import io.ktor.auth.*

data class User(val uid: Int, val email: String, val displayName: String, val password: String) : Principal