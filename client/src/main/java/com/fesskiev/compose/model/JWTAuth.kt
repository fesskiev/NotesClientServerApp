package com.fesskiev.compose.model

import kotlinx.serialization.Serializable

@Serializable
data class JWTAuth(val token: String)