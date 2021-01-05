package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.presentation.AddNoteState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AddNoteUseCase(private val repository: Repository) {

    fun addNote(title: String, description: String, pictureUrl: String?): Flow<AddNoteState> = flow {
        if (title.isEmpty()) {
            return@flow emit(AddNoteState(isEmptyTitle = true))
        }
        if (description.isEmpty()) {
            return@flow emit(AddNoteState(isEmptyDescription = true))
        }
        repository.addNote(title, description, pictureUrl)
        emit(AddNoteState(success = true))
    }
}