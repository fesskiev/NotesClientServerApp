package com.fesskiev.compose.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.fesskiev.compose.domain.NotesUseCase
import com.fesskiev.model.Note

class NotesViewModel(private val state: SavedStateHandle, private val useCase: NotesUseCase) : BaseViewModel() {

    val notesLiveData: MutableLiveData<List<Note>> = MutableLiveData()

    init {
        val savedNotesLiveData = state.getLiveData<List<Note>>("notes")
        Log.w("test", "saved state: ${savedNotesLiveData.value}")
    }

    fun getNotes() {
        launchDataLoad(load = {
            val notes = useCase.getNotes()
            notesLiveData.postValue(notes)
            state.set("notes", notes)
        }, error = {
            Log.w("test", "error: $it")
        })
    }
}