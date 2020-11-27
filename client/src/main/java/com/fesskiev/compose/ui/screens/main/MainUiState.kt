package com.fesskiev.compose.ui.screens.main

import androidx.annotation.StringRes
import com.fesskiev.model.Note

sealed class MainUiState {
    object Loading : MainUiState()
    object Empty : MainUiState()
    data class Error(@StringRes val errorResourceId: Int): MainUiState()
    data class Data(val notes: List<Note>): MainUiState()
}