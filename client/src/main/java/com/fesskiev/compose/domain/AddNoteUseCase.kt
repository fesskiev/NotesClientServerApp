package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.notes.add.AddNoteUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddNoteUseCase(private val repository: Repository) {

    suspend fun addNote(title: String, description: String, pictureUrl: String?): Flow<AddNoteUiState> = flow {
        if (title.isEmpty()) {
            return@flow emit(AddNoteUiState(isEmptyTitle = true))
        }
        if (description.isEmpty()) {
            return@flow emit(AddNoteUiState(isEmptyDescription = true))
        }
        repository.addNote(title, description, pictureUrl)
        emit(AddNoteUiState(success = true))
    }
}