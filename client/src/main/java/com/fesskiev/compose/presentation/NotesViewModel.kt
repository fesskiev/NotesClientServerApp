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

    val notesListUiState = MutableStateFlow(NotesListUiState())
    val addNoteUiState = MutableStateFlow(AddNoteUiState())
    val editNoteUiState = MutableStateFlow(EditNoteUiState())


    init {
        getFirstPageOfNotes()
    }

    private fun getFirstPageOfNotes() {
        viewModelScope.launch {
            notesListUiState.apply {
                update { uiState ->
                    uiState.copy(
                        loading = true,
                        paging = uiState.paging.copy(page = 1),
                    )
                }
                update { uiState ->
                    when (val result = getNotesUseCase(uiState.paging.page)) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                paging = uiState.paging.copy(
                                    endOfPaginationReached = result.data.isEmpty(),
                                    page = uiState.paging.page + 1
                                ),
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
            notesListUiState.apply {
                update { uiState ->
                    uiState.copy(
                        paging = uiState.paging.copy(loadMore = true),
                    )
                }
                update { uiState ->
                    when (val result = getNotesUseCase(uiState.paging.page)) {
                        is Result.Success -> {
                            uiState.copy(
                                paging = uiState.paging.copy(
                                    loadMore = false,
                                    endOfPaginationReached = result.data.isEmpty(),
                                    page = uiState.paging.page + 1,
                                ),
                                notes = uiState.notes?.plus(result.data)
                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                paging = uiState.paging.copy(loadMore = false),
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
            notesListUiState.apply {
                update { uiState ->
                    uiState.copy(
                        refresh = true,
                        paging = uiState.paging.copy(page = 1)
                    )
                }
                update { uiState ->
                    when (val result = getNotesUseCase(uiState.paging.page)) {
                        is Result.Success -> {
                            uiState.copy(
                                refresh = false,
                                paging = uiState.paging.copy(
                                    endOfPaginationReached = result.data.isEmpty(),
                                    page = uiState.paging.page + 1,
                                ),
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
            addNoteUiState.update {
                it.copy(
                    loading = true,
                    error = null
                )
            }
            val addNoteState = addNoteUiState.value
            val result =
                addNoteUseCase(
                    addNoteState.title,
                    addNoteState.description,
                    addNoteState.imageFile
                )
            when (result) {
                is Result.Success -> {
                    addNoteUiState.update {
                        it.copy(
                            loading = false,
                            success = true
                        )
                    }
                    notesListUiState.update {
                        it.copy(notes = it.notes?.plusTop(result.data))
                    }
                }
                is Result.Failure -> {
                    addNoteUiState.update {
                        it.copy(
                            loading = false,
                            addNoteUserInputState = AddNoteUserInputState().copyWithUserInputError(
                                result.e
                            ),
                            error = ErrorState(errorResourceId = parseHttpError(result.e))
                        )
                    }
                }
            }
        }
    }

    fun editNote() {
        viewModelScope.launch {
            editNoteUiState.update {
                it.copy(
                    loading = true,
                    error = null
                )
            }
            val selectedNote = notesListUiState.value.selectedNote!!
            val title = editNoteUiState.value.title
            val description = editNoteUiState.value.description
            val note = selectedNote.copy(
                title = title,
                description = description
            )
            when (val result = editNoteUseCase(note)) {
                is Result.Success -> {
                    editNoteUiState.update {
                        it.copy(
                            loading = false,
                            success = true,
                            error = null
                        )
                    }
                    notesListUiState.update {
                        it.copy(
                            selectedNote = null,
                            notes = it.notes?.replace(note) { n -> n.noteUid == note.noteUid },
                        )
                    }
                }
                is Result.Failure -> {
                    editNoteUiState.update {
                        it.copy(
                            loading = false,
                            error = ErrorState(errorResourceId = parseHttpError(result.e))
                        )
                    }
                }
            }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesListUiState.apply {
                update { uiState ->
                    uiState.copy(
                        loading = true,
                        error = null
                    )
                }
                val result = deleteNoteUseCase(note)
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
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
        notesListUiState.update {
            it.copy(
                loading = false,
                selectedNote = note,
                error = null
            )
        }
    }

    fun openEditNoteScreen(note: Note) {
        editNoteUiState.update {
            it.copy(
                loading = false,
                success = false,
                title = note.title,
                description = note.description,
                error = null
            )
        }
        notesListUiState.update { it.copy(selectedNote = note) }
    }

    fun changeEditNoteTitle(title: String) {
        editNoteUiState.update { it.copy(title = title) }
    }

    fun changeEditNoteDescription(description: String) {
        editNoteUiState.update { it.copy(description = description) }
    }

    fun openAddNoteScreen() {
        addNoteUiState.update { AddNoteUiState() }
    }

    fun deleteAddNoteImage() {
        addNoteUiState.update { it.copy(imageFile = null) }
    }

    fun attachAddNoteImage(imageFile: File) {
        addNoteUiState.update { it.copy(imageFile = imageFile) }
    }

    fun changeAddNoteTitle(title: String) {
        addNoteUiState.update { it.copy(title = title) }
    }

    fun changeAddNoteDescription(description: String) {
        addNoteUiState.update { it.copy(description = description) }
    }
}


