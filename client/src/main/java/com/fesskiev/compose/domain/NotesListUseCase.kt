package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotesListUseCase(private val repository: Repository) {

    fun getNotes(): Flow<List<Note>> = flow {
        emit(repository.getNotes())
    }
}