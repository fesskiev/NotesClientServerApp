package com.fesskiev.compose.domain

import com.fesskiev.ServerErrorCodes.NOTE_DESCRIPTION_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_TITLE_EMPTY
import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.domain.exceptions.EditNoteException
import com.fesskiev.compose.domain.exceptions.UserInputException
import com.fesskiev.model.Note

class EditNoteUseCase(private val repository: Repository) {

    suspend operator fun invoke(note: Note): Result<Unit> =
        try {
            if (note.title.isEmpty()) {
                throw UserInputException(NOTE_TITLE_EMPTY)
            }
            if (note.description.isEmpty()) {
                throw UserInputException(NOTE_DESCRIPTION_EMPTY)
            }
            val result = repository.editNote(note)
            if (result) {
                Result.Success(Unit)
            } else {
                throw EditNoteException()
            }
        } catch (e: Exception) {
            Result.Failure(e)
        }
}
