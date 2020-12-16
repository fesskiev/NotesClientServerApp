package com.fesskiev.compose.ui.screens.notes.add

import androidx.annotation.StringRes

data class AddNoteUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val isEmptyTitle: Boolean = false,
    val isEmptyDescription: Boolean = false,
    @StringRes
    val errorResourceId: Int? = null,
)