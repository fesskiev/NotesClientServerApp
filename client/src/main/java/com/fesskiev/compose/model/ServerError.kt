package com.fesskiev.compose.model

import kotlinx.serialization.Serializable

@Serializable
data class ServerError(val errorCode: Int)