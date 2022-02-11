package com.fesskiev.compose.domain

import com.fesskiev.compose.data.remote.RemoteService
import com.fesskiev.model.Note

class GetNotesUseCase(private val remoteService: RemoteService) {

    suspend operator fun invoke(page: Int): Result<List<Note>> =
        try {
            val notes = remoteService.getNotes(page)
            Result.Success(notes)
        } catch (e: Exception) {
            Result.Failure(e)
        }
}