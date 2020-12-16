package com.fesskiev.compose.ui.screens.notes.list

import androidx.annotation.StringRes
import com.fesskiev.model.Note

data class NotesListUiState(
    val loading: Boolean = false,
    val notes: List<Note>? = null,
    @StringRes
    val errorResourceId: Int? = null,
)