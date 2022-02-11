package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.*
import com.fesskiev.compose.state.*
import com.fesskiev.compose.ui.utils.plusTop
import com.fesskiev.compose.ui.utils.replace
import com.fesskiev.model.Note
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class NotesViewModel(
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    val uiStateFlow = MutableStateFlow(NotesUiState())

    fun getFirstPageOfNotes() {
        viewModelScope.launch {
            uiStateFlow.apply {
                update { uiState ->
                    uiState.copy(
                        loading = true,
                        page = 1,
                    )
                }
                update { uiState ->
                    when (val result = getNotesUseCase(uiState.page)) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                endOfPaginationReached = result.data.isEmpty(),
                                page = uiState.page + 1,
                                notes = result.data
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                error = ErrorState(errorResourceId = parseHttpError(result.e))
                            )
                        }
                    }
                }
            }
        }
    }

    fun loadMore() {
        viewModelScope.launch {
            uiStateFlow.apply {
                update { uiState ->
                    uiState.copy(
                        loadMore = true
                    )
                }
                update { uiState ->
                    when (val result = getNotesUseCase(uiState.page)) {
                        is Result.Success -> {
                            uiState.copy(
                                loadMore = false,
                                endOfPaginationReached = result.data.isEmpty(),
                                page = uiState.page + 1,
                                notes = uiState.notes?.plus(result.data)
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loadMore = false,
                                error = ErrorState(errorResourceId = parseHttpError(result.e))
                            )
                        }
                    }
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            uiStateFlow.apply {
                update { uiState ->
                    uiState.copy(
                        refresh = true,
                        page = 1,
                    )
                }
                update { uiState ->
                    when (val result = getNotesUseCase(uiState.page)) {
                        is Result.Success -> {
                            uiState.copy(
                                refresh = false,
                                endOfPaginationReached = result.data.isEmpty(),
                                page = uiState.page + 1,
                                notes = result.data
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                refresh = false,
                                error = ErrorState(errorResourceId = parseHttpError(result.e))
                            )
                        }
                    }
                }
            }
        }
    }

    fun addNote() {
        viewModelScope.launch {
            uiStateFlow.apply {
                update { uiState ->
                    uiState.copy(loading = true)
                }
                val addNoteState = value.addNoteUiState
                val result =
                    addNoteUseCase(
                        addNoteState.title,
                        addNoteState.description,
                        addNoteState.imageFile
                    )
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                selectedNote = null,
                                notes = uiState.notes?.plusTop(result.data),
                                addNoteUiState = AddNoteUiState(success = true),
                                error = null
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
                                error = ErrorState(errorResourceId = parseHttpError(result.e))
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
                            uiState.copy(
                                loading = false,
                                selectedNote = null,
                                notes = uiState.notes?.replace(note) { it.noteUid == note.noteUid },
                                editNoteUiState = EditNoteUiState(success = true),
                                error = null
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                selectedNote = null,
                                error = ErrorState(errorResourceId = parseHttpError(result.e))
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
                            uiState.copy(
                                loading = false,
                                selectedNote = null,
                                notes = uiState.notes?.minus(note),
                                error = null
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                error = ErrorState(errorResourceId = parseHttpError(result.e))
                            )
                        }
                    }
                }
            }
        }
    }

    fun openNoteDetails(note: Note) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = note,
                error = null
            )
        }
    }

    fun openAddNoteScreen() {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = null,
                addNoteUiState = AddNoteUiState(success = false),
                error = null
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
                error = null
            )
        }
    }

    fun changeEditNoteTitle(title: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                editNoteUiState = it.editNoteUiState.copy(title = title),
                error = null
            )
        }
    }

    fun changeEditNoteDescription(description: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                editNoteUiState = it.editNoteUiState.copy(description = description),
                error = null
            )
        }
    }

    fun deleteAddNoteImage() {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = null,
                addNoteUiState = it.addNoteUiState.copy(imageFile = null),
                error = null
            )
        }
    }

    fun attachAddNoteImage(imageFile: File) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = null,
                addNoteUiState = it.addNoteUiState.copy(imageFile = imageFile),
                error = null
            )
        }
    }

    fun changeAddNoteTitle(title: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = null,
                addNoteUiState = it.addNoteUiState.copy(title = title),
                error = null
            )
        }
    }

    fun changeAddNoteDescription(description: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                selectedNote = null,
                addNoteUiState = it.addNoteUiState.copy(description = description),
                error = null
            )
        }
    }
}


