package com.fesskiev.compose.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val noteUid: Long,
    val userUid: Long,
    val title: String,
    val description: String,
    val pictureName: String?,
    val time: Long
)