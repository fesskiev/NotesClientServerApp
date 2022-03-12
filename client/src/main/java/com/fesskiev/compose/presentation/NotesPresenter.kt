package com.fesskiev.compose.presentation

import androidx.compose.runtime.mutableStateOf
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.*
import com.fesskiev.compose.model.Note
import com.fesskiev.compose.state.*
import com.fesskiev.compose.ui.utils.plusTop
import com.fesskiev.compose.ui.utils.replace
import com.fesskiev.compose.ui.utils.update
import kotlinx.coroutines.launch
import java.io.File

class NotesPresenter(
    private val stateSaver: UiStateSaver,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val addNoteUseCase: AddNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase,
    private val pagingNotesUseCase: PagingNotesUseCase,
    private val refreshNotesUseCase: RefreshNotesUseCase,
    private val searchNotesUseCase: SearchNotesUseCase
) : Presenter() {

    val notesListUiState = mutableStateOf(NotesListUiState())
    val searchNotesUiState = mutableStateOf(SearchNotesUiState())
    val addNoteUiState = mutableStateOf(AddNoteUiState())
    val editNoteUiState = mutableStateOf(EditNoteUiState())

    override fun onCreate() {
        stateSaver.saveStateNotesListener = {
            NotesUiState(
                notesListUiState = notesListUiState.value,
                searchNotesUiState = searchNotesUiState.value,
                addNoteUiState = addNoteUiState.value,
                editNoteUiState = editNoteUiState.value
            )
        }
        coroutineScope.launch {
            val uiState = stateSaver.restoreNotesState()
            if (uiState == null) {
                getFirstPageOfNotes()
            } else {
                notesListUiState.value = uiState.notesListUiState
                searchNotesUiState.value = uiState.searchNotesUiState
                addNoteUiState.value = uiState.addNoteUiState
                editNoteUiState.value = uiState.editNoteUiState
            }
        }
    }

    private fun getFirstPageOfNotes() {
        coroutineScope.launch {
            notesListUiState.apply {
                update { uiState ->
                    uiState.copy(
                        loading = true,
                        paging = uiState.paging.copy(page = 1),
                        error = null
                    )
                }
                update { uiState ->
                    when (val result = pagingNotesUseCase(uiState.paging.page)) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                paging = uiState.paging.copy(
                                    endOfPaginationReached = result.data.list.isEmpty(),
                                    page = uiState.paging.page + 1,
                                    pagingSource = result.data.pagingSource
                                ),
                                notes = result.data.list,
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

    fun searchNotes(query: String) {
        coroutineScope.launch {
            searchNotesUiState.apply {
                update { uiState ->
                    uiState.copy(
                        loading = true,
                        query = query,
                        error = null
                    )
                }
                update { uiState ->
                    when (val result = searchNotesUseCase(query)) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                notes = result.data,
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

    fun loadMore() {
        coroutineScope.launch {
            notesListUiState.apply {
                update { uiState ->
                    uiState.copy(
                        paging = uiState.paging.copy(loadMore = true),
                        error = null
                    )
                }
                update { uiState ->
                    when (val result = pagingNotesUseCase(uiState.paging.page)) {
                        is Result.Success -> {
                            uiState.copy(
                                paging = uiState.paging.copy(
                                    loadMore = false,
                                    endOfPaginationReached = result.data.list.isEmpty(),
                                    page = uiState.paging.page + 1,
                                    pagingSource = result.data.pagingSource
                                ),
                                notes = uiState.notes?.plus(result.data.list),
                                error = null
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
        coroutineScope.launch {
            notesListUiState.apply {
                update { uiState ->
                    uiState.copy(
                        refresh = true,
                        paging = uiState.paging.copy(page = 1),
                        error = null
                    )
                }
                update { uiState ->
                    when (val result = refreshNotesUseCase(uiState.paging.page)) {
                        is Result.Success -> {
                            uiState.copy(
                                refresh = false,
                                paging = uiState.paging.copy(
                                    endOfPaginationReached = result.data.list.isEmpty(),
                                    page = uiState.paging.page + 1,
                                    pagingSource = result.data.pagingSource
                                ),
                                notes = result.data.list,
                                error = null
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
        coroutineScope.launch {
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
                            success = true,
                            error = null
                        )
                    }
                    notesListUiState.update {
                        it.copy(
                            notes = it.notes?.plusTop(result.data),
                            error = null
                        )
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
        coroutineScope.launch {
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
        coroutineScope.launch {
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