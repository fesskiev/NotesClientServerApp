package com.fesskiev.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerError(val message: String)