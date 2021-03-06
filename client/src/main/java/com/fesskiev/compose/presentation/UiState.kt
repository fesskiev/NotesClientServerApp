package com.fesskiev.compose.presentation

import androidx.annotation.StringRes
import com.fesskiev.model.Note

data class AuthUiState(
    val loading: Boolean = false,
    val displayName: String = "",
    val email: String = "",
    val password: String = "",
    val isLoginFormShow: Boolean = true,
    val authState: AuthState = AuthState(),
    @StringRes
    val errorResourceId: Int? = null
)

data class AuthState(
    val success: Boolean = false,
    val isEmptyPasswordError: Boolean = false,
    val isEmptyEmailError: Boolean = false,
    val isEmptyDisplayNameError: Boolean = false,
    val isValidateEmailError: Boolean = false,
    val isValidatePasswordError: Boolean = false
) {
    companion object {
        val Default = AuthState()
    }
}

data class NotesListUiState(
    val loading: Boolean = false,
    val paging: Boolean = false,
    val needLoadMore: Boolean = true,
    val notes: List<Note>? = null,
    val page: Int = 1,
    @StringRes
    val errorResourceId: Int? = null
)

data class DeleteNoteState(
    val notes: List<Note>? = null,
    @StringRes
    val errorResourceId: Int? = null
)

data class AddNoteUiState(
    val loading: Boolean = false,
    val title: String = "",
    val description: String = "",
    val addNoteState: AddNoteState = AddNoteState(),
    @StringRes
    val errorResourceId: Int? = null
)

data class AddNoteState(
    val success: Boolean = false,
    val isEmptyTitle: Boolean = false,
    val isEmptyDescription: Boolean = false
) {
    companion object {
        val Default = AddNoteState()
    }
}

data class EditNoteUiState(
    val loading: Boolean = false,
    val title: String = "",
    val description: String = "",
    val editNoteState: EditNoteState = EditNoteState(),
    @StringRes
    val errorResourceId: Int? = null
)

data class EditNoteState(
    val success: Boolean = false,
    val isEmptyTitle: Boolean = false,
    val isEmptyDescription: Boolean = false
) {
    companion object {
        val Default = EditNoteState()
    }
}

data class NoteDetailsUiState(
    val loading: Boolean = false,
    val note: Note? = null,
    @StringRes
    val errorResourceId: Int? = null
)

data class SettingsUiState(
    val loading: Boolean = false,
    val isLogout: Boolean = false,
    val themeMode: String = "",
    val isThemeModePopupShow: Boolean = false,
    @StringRes
    val errorResourceId: Int? = null
)

