package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.GetNoteByIdUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotesDetailsViewModel(private val getNoteByIdUseCase: GetNoteByIdUseCase) : ViewModel() {

    val stateFlow = MutableStateFlow(NoteDetailsUiState())

    fun getNoteByUid(noteUid: Int) {
        viewModelScope.launch {
            getNoteByIdUseCase.getNoteByUid(noteUid)
                .onStart {
                    stateFlow.value = NoteDetailsUiState(loading = true)
                }
                .catch {
                    stateFlow.value = NoteDetailsUiState(errorResourceId = parseHttpError(it))
                }.collect {
                    stateFlow.value = NoteDetailsUiState(note = it)
                }
        }
    }
}