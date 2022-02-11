package com.fesskiev.compose.state

import androidx.annotation.StringRes
import com.fesskiev.ServerErrorCodes.DISPLAY_NAME_EMPTY
import com.fesskiev.ServerErrorCodes.EMAIL_EMPTY
import com.fesskiev.ServerErrorCodes.EMAIL_INVALID
import com.fesskiev.ServerErrorCodes.NOTE_DESCRIPTION_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_TITLE_EMPTY
import com.fesskiev.ServerErrorCodes.PASSWORD_EMPTY
import com.fesskiev.ServerErrorCodes.PASSWORD_INVALID
import com.fesskiev.compose.domain.exceptions.UserInputException
import com.fesskiev.model.Note
import java.io.File
import kotlin.random.Random

data class AuthUiState(
    val loading: Boolean = false,
    val displayName: String = "",
    val email: String = "test15@i.ua",
    val password: String = "123456",
    val isLoginFormShow: Boolean = true,
    val authUserInputState: AuthUserInputState = AuthUserInputState(),
    val error: ErrorState? = null
)

data class AuthUserInputState(
    val success: Boolean = false,
    val isEmptyPasswordError: Boolean = false,
    val isEmptyEmailError: Boolean = false,
    val isEmptyDisplayNameError: Boolean = false,
    val isValidateEmailError: Boolean = false,
    val isValidatePasswordError: Boolean = false
)

fun AuthUserInputState.copyWithUserInputError(e: Exception): AuthUserInputState =
    when (e) {
        is UserInputException -> {
            when (e.errorCode) {
                PASSWORD_EMPTY -> this.copy(isEmptyPasswordError = true)
                EMAIL_EMPTY -> this.copy(isEmptyEmailError = true)
                DISPLAY_NAME_EMPTY -> this.copy(isEmptyDisplayNameError = true)
                EMAIL_INVALID -> this.copy(isValidateEmailError = true)
                PASSWORD_INVALID -> this.copy(isValidatePasswordError = true)
                else -> this
            }
        }
        else -> this
    }

data class SettingsUiState(
    val loading: Boolean = false,
    val isLogout: Boolean = false,
    val themeMode: String = "",
    val error: ErrorState? = null
)

data class EditNoteUiState(
    val success: Boolean = false,
    val title: String = "",
    val description: String = ""
)

data class AddNoteUiState(
    val success: Boolean = false,
    val imageFile: File? = null,
    val addNoteUserInputState: AddNoteUserInputState = AddNoteUserInputState(),
    val title: String = "",
    val description: String = ""
)

data class AddNoteUserInputState(
    val isEmptyTitleError: Boolean = false,
    val isEmptyDescriptionError: Boolean = false,
)

fun AddNoteUserInputState.copyWithUserInputError(e: Exception): AddNoteUserInputState =
    when (e) {
        is UserInputException -> {
            when (e.errorCode) {
                NOTE_TITLE_EMPTY -> this.copy(isEmptyTitleError = true)
                NOTE_DESCRIPTION_EMPTY -> this.copy(isEmptyDescriptionError = true)
                else -> this
            }
        }
        else -> this
    }

data class NotesUiState(
    val notes: List<Note>? = null,
    val page: Int = -1,
    val loading: Boolean = false,
    val refresh: Boolean = false,
    val loadMore: Boolean = false,
    val endOfPaginationReached: Boolean = false,
    val selectedNote: Note? = null,
    val editNoteUiState: EditNoteUiState = EditNoteUiState(),
    val addNoteUiState: AddNoteUiState = AddNoteUiState(),
    val error: ErrorState? = null
) {
    override fun toString(): String {
        return "NotesUiState(page=$page, size=${notes?.size} loading=$loading, loadMore=$loadMore, refresh=$refresh, endPagination=$endOfPaginationReached)"
    }
}

data class ErrorState(
    val id: Int = Random.nextInt(),
    @StringRes
    val errorResourceId: Int? = null
)

