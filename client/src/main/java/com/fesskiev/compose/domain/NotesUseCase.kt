package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.model.Note

class NotesUseCase(private val repository: Repository) {

    suspend fun getCurrentWeatherByCity() : List<Note> {
        return repository.getNotes()
    }
}