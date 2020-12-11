package com.fesskiev.compose.domain

import com.fesskiev.compose.R
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.notes.NotesUiState
import com.fesskiev.model.Note

class EditNoteUseCase(private val repository: Repository) {

    suspend fun editNote(note: Note): NotesUiState {
        val result = repository.editNote(note)
        if (result) {
            return NotesUiState.Data(repository.getNotes())
        }
        return NotesUiState.Error(R.string.error_edit_note)
    }
}