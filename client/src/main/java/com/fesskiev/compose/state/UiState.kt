package com.fesskiev.compose.state

import androidx.annotation.StringRes
import com.fesskiev.ServerErrorCodes.DISPLAY_NAME_EMPTY
import com.fesskiev.ServerErrorCodes.EMAIL_EMPTY
import com.fesskiev.ServerErrorCodes.EMAIL_INVALID
import com.fesskiev.ServerErrorCodes.NOTE_DESCRIPTION_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_TITLE_EMPTY
import com.fesskiev.ServerErrorCodes.PASSWORD_EMPTY
import com.fesskiev.ServerErrorCodes.PASSWORD_INVALID
import com.fesskiev.compose.data.PagingSource
import com.fesskiev.compose.domain.exceptions.UserInputException
import com.fesskiev.compose.model.Note
import java.io.File
import kotlin.random.Random

data class AuthUiState(
    val loading: Boolean = false,
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
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
    val loading: Boolean = false,
    val success: Boolean = false,
    val title: String = "",
    val description: String = "",
    val error: ErrorState? = null
)

data class AddNoteUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val imageFile: File? = null,
    val addNoteUserInputState: AddNoteUserInputState = AddNoteUserInputState(),
    val title: String = "",
    val description: String = "",
    val error: ErrorState? = null
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

data class SearchNotesUiState(
    val notes: List<Note>? = null,
    val query: String = "",
    val loading: Boolean = false,
    val error: ErrorState? = null
) {
    override fun toString(): String {
        return "list: size=${notes?.size}, query=$query loading=$loading, error=$error)"
    }
}

data class NotesListUiState(
    val notes: List<Note>? = null,
    val loading: Boolean = false,
    val refresh: Boolean = false,
    val paging: PagingState = PagingState(),
    val selectedNote: Note? = null,
    val error: ErrorState? = null
) {

    override fun toString(): String {
        val listState = "list: size=${notes?.size}, loading=$loading, refresh=$refresh, selectedNote=$selectedNote)"
        val pagingState = "paging: next page=${paging.page}, more=${paging.loadMore}, endReached=${paging.endOfPaginationReached}, source=${paging.pagingSource} "
        val errorState = "error: id=${error?.errorResourceId}"
        return listState + "\n" + pagingState + "\n" + errorState
    }
}

data class PagingState(
    val page: Int = -1,
    val loadMore: Boolean = false,
    val endOfPaginationReached: Boolean = false,
    val pagingSource: PagingSource = PagingSource.LOCAL
)

data class ErrorState(
    val id: Int = Random.nextInt(),
    @StringRes
    val errorResourceId: Int? = null
)

