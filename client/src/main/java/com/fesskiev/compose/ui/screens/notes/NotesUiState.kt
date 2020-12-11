package com.fesskiev.compose.ui.screens.notes

import androidx.annotation.StringRes
import com.fesskiev.model.Note

sealed class NotesUiState {
    object Empty : NotesUiState()
    data class Data(val notes: List<Note>) : NotesUiState()
    object Loading : NotesUiState()
    data class Error(@StringRes val errorResourceId: Int) : NotesUiState()
}