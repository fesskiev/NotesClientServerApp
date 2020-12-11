package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.notes.NotesUiState

class NotesUseCase(private val repository: Repository) {

    suspend fun getNotes(): NotesUiState {
        val notes = repository.getNotes()
        if (notes.isEmpty()) {
            return NotesUiState.Empty
        }
        return NotesUiState.Data(notes)
    }
}