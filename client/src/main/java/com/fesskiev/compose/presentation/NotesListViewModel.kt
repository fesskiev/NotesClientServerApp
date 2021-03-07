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

    fun getNotes(page: Int) {
        viewModelScope.launch {
            notesListUseCase.getNotes(page = page, paging = false)
                .onStart {
                    stateFlow.value =
                        stateFlow.value.copy(loading = true, paging = false, errorResourceId = null)
                }
                .catch {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            paging = false,
                            errorResourceId = parseHttpError(it)
                        )
                }.collect {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            paging = false,
                            notes = it,
                            errorResourceId = null
                        )
                }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteUseCase.deleteNote(note)
                .onStart {
                    stateFlow.value =
                        stateFlow.value.copy(loading = true, paging = false, errorResourceId = null)
                }
                .catch {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            paging = false,
                            errorResourceId = parseHttpError(it)
                        )
                }.collect {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            notes = it.notes,
                            errorResourceId = it.errorResourceId
                        )
                }
        }
    }

    fun loadMoreNotes() {
        viewModelScope.launch {
            val page = stateFlow.value.page + 1
            val oldNotesSize = stateFlow.value.notes?.size
            notesListUseCase.getNotes(page = page, paging = true)
                .onStart {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            paging = true,
                            needLoadMore = true,
                            errorResourceId = null
                        )
                }
                .catch {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            paging = false,
                            needLoadMore = true,
                            errorResourceId = parseHttpError(it)
                        )
                }.collect {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            paging = false,
                            needLoadMore = oldNotesSize != it.size,
                            page = page,
                            notes = it,
                            errorResourceId = null
                        )
                }
        }
    }
}