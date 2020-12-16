package com.fesskiev.compose.domain

import com.fesskiev.compose.R
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.notes.list.NotesListUiState
import com.fesskiev.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EditNoteUseCase(private val repository: Repository) {

    suspend fun editNote(note: Note): Flow<NotesListUiState> = flow {
        val result = repository.editNote(note)
        if (result) {
            val notes = repository.getNotes()
            return@flow emit(NotesListUiState(notes = notes))
        }
        emit(NotesListUiState(errorResourceId = R.string.error_edit_note))
    }
}