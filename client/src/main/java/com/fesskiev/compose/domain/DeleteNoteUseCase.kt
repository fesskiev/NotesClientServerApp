package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.domain.exceptions.DeleteNoteException
import com.fesskiev.model.Note

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
            e.printStackTrace()
            Result.Failure(e)
        }
}
