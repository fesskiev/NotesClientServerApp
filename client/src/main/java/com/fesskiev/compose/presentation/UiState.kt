package com.fesskiev.compose.presentation

import androidx.annotation.StringRes
import com.fesskiev.model.Note

data class AuthUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val isEmptyPasswordError: Boolean = false,
    val isEmptyEmailError: Boolean = false,
    val isEmptyDisplayNameError: Boolean = false,
    val isValidateEmailError: Boolean = false,
    val isValidatePasswordError: Boolean = false,
    @StringRes
    val errorResourceId: Int? = null,
)

data class NotesListUiState(
    val loading: Boolean = false,
    val notes: List<Note>? = null,
    @StringRes
    val errorResourceId: Int? = null,
)

data class AddNoteUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val isEmptyTitle: Boolean = false,
    val isEmptyDescription: Boolean = false,
    @StringRes
    val errorResourceId: Int? = null,
)

data class EditNoteUiState(
    val loading: Boolean = false,
    val note: Note? = null,
    val success: Boolean = false,
    val isEmptyTitle: Boolean = false,
    val isEmptyDescription: Boolean = false,
    @StringRes
    val errorResourceId: Int? = null,
)