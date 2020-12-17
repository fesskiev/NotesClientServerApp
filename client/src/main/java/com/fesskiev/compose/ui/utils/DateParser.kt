package com.fesskiev.compose.ui.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun formatDate(milli: Long): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    val instant = Instant.ofEpochMilli(milli)
    val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
    return formatter.format(date)
}