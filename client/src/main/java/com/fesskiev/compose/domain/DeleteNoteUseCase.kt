package com.fesskiev.compose.domain

import com.fesskiev.compose.R
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.presentation.NotesListUiState
import com.fesskiev.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DeleteNoteUseCase(private val repository: Repository) {

    fun deleteNote(note: Note): Flow<NotesListUiState> = flow {
        val result = repository.deleteNote(note)
        if (result) {
            val notes = repository.getNotes()
            return@flow emit(NotesListUiState(notes = notes))
        }
        emit(NotesListUiState(errorResourceId = R.string.error_delete_note))
    }
}