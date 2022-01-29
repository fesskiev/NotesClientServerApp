package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.model.Note

class GetNotesUseCase(private val repository: Repository) {

    suspend operator fun invoke(page: Int): List<Note> = repository.getNotes(page)
}