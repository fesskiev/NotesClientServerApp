package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.presentation.NotesListUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotesListUseCase(private val repository: Repository) {

    fun getNotes(): Flow<NotesListUiState> = flow {
        val notes = repository.getNotes()
        emit(NotesListUiState(notes = notes))
    }
}