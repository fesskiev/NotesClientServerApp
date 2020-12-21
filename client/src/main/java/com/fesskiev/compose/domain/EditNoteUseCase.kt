package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.presentation.EditNoteUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EditNoteUseCase(private val repository: Repository) {

    fun editNote(
        noteUid: Int,
        title: String,
        description: String,
        pictureUrl: String?
    ): Flow<EditNoteUiState> = flow {
        if (title.isEmpty()) {
            return@flow emit(EditNoteUiState(isEmptyTitle = true))
        }
        if (description.isEmpty()) {
            return@flow emit(EditNoteUiState(isEmptyDescription = true))
        }
        repository.editNote(noteUid, title, description, pictureUrl)
        emit(EditNoteUiState(success = true))
    }
}