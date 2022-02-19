package com.fesskiev.compose.domain

import com.fesskiev.compose.data.PagingResult
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.model.Note

class PagingNotesUseCase(private val repository: Repository) {

    suspend operator fun invoke(page: Int): Result<PagingResult<Note>> =
        try {
            val pagingResult = repository.pagingNotes(page)
            Result.Success(pagingResult)
        } catch (e: Exception) {
            Result.Failure(e)
        }
}