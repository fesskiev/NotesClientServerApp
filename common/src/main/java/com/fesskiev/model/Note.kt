package com.fesskiev.model

import kotlinx.serialization.Serializable

@Serializable
data class Note(val noteUid: Int, val userUid: Int,val text: String, val time: Long)