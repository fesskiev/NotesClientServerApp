package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.main.MainUiState
import com.fesskiev.model.Note

class NotesUseCase(private val repository: Repository) {

    suspend fun getNotes() : MainUiState {
        val notes = repository.getNotes()
        return if (notes.isEmpty()) {
            MainUiState.Empty
        } else {
            MainUiState.Data(notes)
        }
    }
}