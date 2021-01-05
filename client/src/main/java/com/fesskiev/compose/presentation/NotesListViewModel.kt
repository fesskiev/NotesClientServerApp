package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.DeleteNoteUseCase
import com.fesskiev.compose.domain.NotesListUseCase
import com.fesskiev.model.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NotesListViewModel(
    private val notesListUseCase: NotesListUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    val stateFlow = MutableStateFlow(NotesListUiState())

    fun getNotes() {
        viewModelScope.launch {
            notesListUseCase.getNotes()
                .onStart {
                    stateFlow.value = stateFlow.value.copy(loading = true, errorResourceId = null)
                }
                .catch {
                    stateFlow.value =
                        stateFlow.value.copy(loading = false, errorResourceId = parseHttpError(it))
                }.collect {
                    stateFlow.value =
                        stateFlow.value.copy(loading = false, notes = it, errorResourceId = null)
                }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase.deleteNote(note)
                .onStart {
                    stateFlow.value = stateFlow.value.copy(loading = true, errorResourceId = null)
                }
                .catch {
                    stateFlow.value =
                        stateFlow.value.copy(loading = false, errorResourceId = parseHttpError(it))
                }.collect {
                    stateFlow.value =
                        stateFlow.value.copy(loading = false, notes = it.notes, errorResourceId = it.errorResourceId)
                }
        }
    }
}