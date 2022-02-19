package com.fesskiev.compose.model


import kotlinx.serialization.Serializable

@Serializable
data class User(val uid: Long, val email: String, val displayName: String, val password: String)