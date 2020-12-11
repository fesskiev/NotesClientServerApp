package com.fesskiev.compose.presentation

import androidx.lifecycle.MutableLiveData
import com.fesskiev.compose.domain.DeleteNoteUseCase
import com.fesskiev.compose.domain.EditNoteUseCase
import com.fesskiev.compose.domain.NotesUseCase
import com.fesskiev.compose.ui.screens.notes.NotesUiState
import com.fesskiev.model.Note

class NotesViewModel(
    private val notesUseCase: NotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val editNoteUseCase: EditNoteUseCase
) : BaseViewModel() {

    val liveData: MutableLiveData<NotesUiState> = MutableLiveData()

    init {
        liveData.postValue(NotesUiState.Empty)
    }

    fun getNotes() {
        liveData.postValue(NotesUiState.Loading)
        launchDataLoad(load = {
            val notesUiState = notesUseCase.getNotes()
            liveData.postValue(notesUiState)
        }, error = {
            liveData.postValue(NotesUiState.Error(it))
        })
    }

    fun deleteNote(note: Note) {
        liveData.postValue(NotesUiState.Loading)
        launchDataLoad(load = {
            val notesUiState = deleteNoteUseCase.deleteNote(note)
            liveData.postValue(notesUiState)
        }, error = {
            liveData.postValue(NotesUiState.Error(it))
        })
    }

    fun editNote(note: Note) {
        liveData.postValue(NotesUiState.Loading)
        launchDataLoad(load = {
            val notesUiState = editNoteUseCase.editNote(note)
            liveData.postValue(notesUiState)
        }, error = {
            liveData.postValue(NotesUiState.Error(it))
        })
    }
}