package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.AddNoteUseCase
import com.fesskiev.compose.domain.DeleteNoteUseCase
import com.fesskiev.compose.domain.EditNoteUseCase
import com.fesskiev.compose.domain.Result
import com.fesskiev.compose.mvi.*
import com.fesskiev.model.Note
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

class NotesViewModel(
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    notesPager: Pager<Int, Note>
) : ViewModel() {

    private val action = MutableStateFlow<Action>(Action.Idle)
    val uiStateFlow = MutableStateFlow(NotesUiState())
    val notesPagingStateFlow: Flow<PagingData<Note>> = notesPager
        .flow
        .cachedIn(viewModelScope)
        .combine(action) { pagingData, action -> reducer(pagingData, action) }

    fun addNote() {
        viewModelScope.launch {
            uiStateFlow.apply {
                update { uiState ->
                    uiState.copy(loading = true)
                }
                val addNoteState = value.addNoteUiState
                val result =
                    addNoteUseCase(addNoteState.title, addNoteState.description, addNoteState.imageFile)
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            dispatchAction(Action.AddNote(result.data))
                            uiState.copy(
                                loading = false,
                                selectedNote = null,
                                addNoteUiState = AddNoteUiState(success = true),
                                errorResourceId = null
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                selectedNote = null,
                                addNoteUiState = uiState.addNoteUiState.copy(
                                    addNoteUserInputState = AddNoteUserInputState().copyWithUserInputError(
                                        result.e
                                    )
                                ),
                                errorResourceId = parseHttpError(result.e)
                            )
                        }
                    }
                }
            }
        }
    }

    fun editNote() {
        viewModelScope.launch {
            uiStateFlow.apply {
                update { uiState ->
                    uiState.copy(loading = true)
                }
                val selectedNote = value.selectedNote!!
                val title = value.editNoteUiState.title
                val description = value.editNoteUiState.description

                val note = selectedNote.copy(
                    title = title,
                    description = description
                )
                val result = editNoteUseCase(note)
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            dispatchAction(Action.EditNote(note))
                            uiState.copy(
                                loading = false,
                                selectedNote = null,
                                editNoteUiState = EditNoteUiState(success = true),
                                errorResourceId = null
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                selectedNote = null,
                                errorResourceId = parseHttpError(result.e)
                            )
                        }
                    }
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            uiStateFlow.apply {
                update { uiState ->
                    uiState.copy(loading = true)
                }
                val result = deleteNoteUseCase(note)
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            dispatchAction(Action.DeleteNote(note))
                            uiState.copy(
                                loading = false,
                                selectedNote = null,
                                errorResourceId = null
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                errorResourceId = parseHttpError(result.e)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun reducer(paging: PagingData<Note>, action: Action): PagingData<Note> {
        when (action) {
            is Action.DeleteNote -> return paging.filter { action.note.noteUid != it.noteUid }
            is Action.AddNote -> {
                return paging.insertHeaderItem(item = action.note)
            }
            is Action.EditNote -> {
                return paging.map {
                    if (action.note.noteUid == it.noteUid) {
                        return@map it.copy(
                            title = action.note.title,
                            description = action.note.description
                        )
                    }
                    return@map it
                }
            }
        }
        return paging
    }

    fun refresh() {
        uiStateFlow.update {
            it.copy(
                loading = false,
                refresh = true,
                selectedNote = null,
                errorResourceId = null
            )
        }
    }

    fun openNoteDetails(note: Note) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = note,
                errorResourceId = null
            )
        }
    }

    fun openAddNoteScreen() {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = null,
                addNoteUiState = AddNoteUiState(success = false),
                errorResourceId = null
            )
        }
    }

    fun openEditNoteScreen(note: Note) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = note,
                editNoteUiState = EditNoteUiState(
                    success = false,
                    title = note.title,
                    description = note.description
                ),
                errorResourceId = null
            )
        }
    }

    fun changeEditNoteTitle(title: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                editNoteUiState = it.editNoteUiState.copy(title = title),
                errorResourceId = null
            )
        }
    }

    fun changeEditNoteDescription(description: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                editNoteUiState = it.editNoteUiState.copy(description = description),
                errorResourceId = null
            )
        }
    }

    fun deleteAddNoteImage() {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = null,
                addNoteUiState = it.addNoteUiState.copy(imageFile = null),
                errorResourceId = null
            )
        }
    }

    fun attachAddNoteImage(imageFile: File) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = null,
                addNoteUiState = it.addNoteUiState.copy(imageFile = imageFile),
                errorResourceId = null
            )
        }
    }

    fun changeAddNoteTitle(title: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = null,
                addNoteUiState = it.addNoteUiState.copy(title = title),
                errorResourceId = null
            )
        }
    }

    fun changeAddNoteDescription(description: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = null,
                addNoteUiState = it.addNoteUiState.copy(description = description),
                errorResourceId = null
            )
        }
    }

    private fun dispatchAction(action: Action) {
        this.action.value = action
    }
}


