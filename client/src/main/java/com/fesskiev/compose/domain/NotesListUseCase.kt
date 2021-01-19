package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.data.RepositoryImpl
import com.fesskiev.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class NotesListUseCase(private val repository: Repository) {

    fun getNotes(page: Int, paging: Boolean): Flow<List<Note>> = flow {
        if (paging) {
            (repository as RepositoryImpl).isNotesCacheExpired = true
        }
        emit(repository.getNotes(page))
    }
}