package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.domain.exceptions.DeleteNoteException
import com.fesskiev.compose.model.Note

class DeleteNoteUseCase(private val repository: Repository) {

    suspend operator fun invoke(note: Note): Result<Unit> =
        try {
            val result = repository.deleteNote(note)
            if (result) {
                Result.Success(Unit)
            } else {
                throw DeleteNoteException()
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
}
