package com.fesskiev.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(
    val noteUid: Int,
    val userUid: Int,
    val title: String,
    val description: String,
    val pictureUrl: String?,
    val time: Long
)