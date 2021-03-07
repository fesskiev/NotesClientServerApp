package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.presentation.AddNoteState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File

class AddNoteUseCase(private val repository: Repository) {

    fun addNote(title: String, description: String, file: File?): Flow<AddNoteState> = flow {
        if (title.isEmpty()) {
            return@flow emit(AddNoteState(isEmptyTitle = true))
        }
        if (description.isEmpty()) {
            return@flow emit(AddNoteState(isEmptyDescription = true))
        }
        val note = repository.addNote(title, description)
        file?.let {
            repository.addImage(note, it)
        }
        emit(AddNoteState(success = true))
    }
}