package com.fesskiev.model

import io.ktor.auth.*

data class User(val uid: String, val email: String, val displayName: String, val password: String) : Principal