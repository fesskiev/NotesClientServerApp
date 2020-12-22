package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.presentation.EditNoteUiState
import com.fesskiev.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNoteByIdUseCase(private val repository: Repository) {

    fun getNoteByUid(noteUid: Int) : Flow<Note> = flow {
        val note = repository.getNoteById(noteUid)
        emit(note)
    }
}