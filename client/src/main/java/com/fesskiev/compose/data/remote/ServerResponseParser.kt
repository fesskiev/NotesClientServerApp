package com.fesskiev.compose.data.remote

import androidx.annotation.StringRes
import com.fesskiev.ServerErrorCodes.DISPLAY_NAME_EMPTY
import com.fesskiev.ServerErrorCodes.EMAIL_ALREADY_USE
import com.fesskiev.ServerErrorCodes.EMAIL_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_DESCRIPTION_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_NOT_ADDED
import com.fesskiev.ServerErrorCodes.NOTE_TITLE_EMPTY
import com.fesskiev.ServerErrorCodes.PASSWORD_EMPTY
import com.fesskiev.ServerErrorCodes.PASSWORD_INCORRECT
import com.fesskiev.ServerErrorCodes.SESSION_NOT_FOUND
import com.fesskiev.ServerErrorCodes.USER_NOT_CREATED
import com.fesskiev.ServerErrorCodes.USER_NOT_FOUND
import com.fesskiev.compose.R
import com.fesskiev.model.JWTAuth
import com.fesskiev.model.ServerError
import kotlinx.serialization.json.Json

fun parseJWT(body: String): JWTAuth? = try {
    Json.decodeFromString(JWTAuth.serializer(), body)
} catch (e: Exception) {
    null
}

@StringRes
fun parseServerError(body: String): Int = try {
    val serverError = Json.decodeFromString(ServerError.serializer(), body)
    parseServerErrorCode(serverError.errorCode)
} catch (e: Exception) {
    R.string.error_unknown
}

fun parseServerErrorCode(errorCode: Int): Int = when (errorCode) {
    EMAIL_EMPTY -> R.string.error_empty_email
    DISPLAY_NAME_EMPTY -> R.string.error_empty_display_name
    PASSWORD_EMPTY -> R.string.error_empty_email
    EMAIL_ALREADY_USE -> R.string.error_email_already_use
    USER_NOT_FOUND -> R.string.error_user_not_found
    PASSWORD_INCORRECT -> R.string.error_password_incorrect
    USER_NOT_CREATED -> R.string.error_user_not_created
    SESSION_NOT_FOUND -> R.string.error_session_not_found
    NOTE_NOT_ADDED -> R.string.error_note_not_added
    NOTE_EMPTY -> R.string.error_note_empty
    NOTE_TITLE_EMPTY -> R.string.error_note_title_empty
    NOTE_DESCRIPTION_EMPTY -> R.string.error_note_description_empty
    else -> R.string.error_unknown
}

