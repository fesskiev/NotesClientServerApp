package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.model.Note

class SearchNotesUseCase(private val repository: Repository) {

    suspend operator fun invoke(query: String): Result<List<Note>> {
        try {
            if (query.isEmpty()) {
                return Result.Success(listOf())
            }
            val notes = repository.searchNotes(query)
            return Result.Success(notes)
        } catch (e: Exception) {
            return Result.Failure(e)
        }
    }
}
