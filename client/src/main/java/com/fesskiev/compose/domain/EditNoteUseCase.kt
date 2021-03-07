package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.presentation.EditNoteState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class EditNoteUseCase(private val repository: Repository) {

    fun editNote(noteUid: Int, title: String, description: String): Flow<EditNoteState> = flow {
        if (title.isEmpty()) {
            return@flow emit(EditNoteState(isEmptyTitle = true))
        }
        if (description.isEmpty()) {
            return@flow emit(EditNoteState(isEmptyDescription = true))
        }
        repository.editNote(noteUid, title, description)
        emit(EditNoteState(success = true))
    }
}