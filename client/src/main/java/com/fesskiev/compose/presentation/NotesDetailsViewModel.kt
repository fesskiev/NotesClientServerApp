package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.GetNoteByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NotesDetailsViewModel(private val getNoteByIdUseCase: GetNoteByIdUseCase) : ViewModel() {

    val stateFlow = MutableStateFlow(NoteDetailsUiState())

    fun getNoteByUid(noteUid: Int) {
        viewModelScope.launch {
            getNoteByIdUseCase.getNoteByUid(noteUid)
                .onStart {
                    stateFlow.value = stateFlow.value.copy(loading = true, errorResourceId = null)
                }
                .catch {
                    stateFlow.value = stateFlow.value.copy(loading = false, errorResourceId = parseHttpError(it))
                }.collect {
                    stateFlow.value = stateFlow.value.copy(loading = false, note = it, errorResourceId = null)
                }
        }
    }
}