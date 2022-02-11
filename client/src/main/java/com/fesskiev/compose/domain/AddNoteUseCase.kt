package com.fesskiev.compose.domain

import com.fesskiev.ServerErrorCodes.NOTE_DESCRIPTION_EMPTY
import com.fesskiev.ServerErrorCodes.NOTE_TITLE_EMPTY
import com.fesskiev.compose.data.remote.RemoteService
import com.fesskiev.compose.domain.exceptions.UserInputException
import com.fesskiev.model.Note
import java.io.File

class AddNoteUseCase(private val remoteService: RemoteService) {

    suspend operator fun invoke(title: String, description: String, file: File?): Result<Note> =
        try {
            if (title.isEmpty()) {
                throw UserInputException(NOTE_TITLE_EMPTY)
            }
            if (description.isEmpty()) {
                throw UserInputException(NOTE_DESCRIPTION_EMPTY)
            }
            var note = remoteService.addNote(title, description)
            file?.let {
               note = remoteService.addImage(note, it)
            }
            Result.Success(note)
        } catch (e: Exception) {
            Result.Failure(e)
        }
}