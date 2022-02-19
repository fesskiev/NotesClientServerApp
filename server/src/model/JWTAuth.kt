package com.fesskiev.model

import kotlinx.serialization.Serializable

@Serializable
data class JWTAuth(val token: String)