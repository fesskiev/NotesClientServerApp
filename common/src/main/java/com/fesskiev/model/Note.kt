package com.fesskiev.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(val uid: String, val text: String, val time: Long)