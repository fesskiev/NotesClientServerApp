package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.EditNoteUseCase
import com.fesskiev.compose.domain.GetNoteByIdUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class EditNoteViewModel(
    private val editNoteUseCase: EditNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase
) : ViewModel() {

    val stateFlow = MutableStateFlow(EditNoteUiState())

    fun editNote(noteUid: Int, title: String, description: String, pictureUrl: String?) {
        viewModelScope.launch {
            editNoteUseCase.editNote(noteUid, title, description, pictureUrl)
                .onStart {
                    stateFlow.value = EditNoteUiState(loading = true)
                }
                .catch {
                    stateFlow.value = EditNoteUiState(errorResourceId = parseHttpError(it))
                }.collect {
                    stateFlow.value = it
                }
        }
    }

    fun getNoteByUid(noteUid: Int) {
        viewModelScope.launch {
            getNoteByIdUseCase.getNoteByUid(noteUid)
                .onStart {
                    stateFlow.value = EditNoteUiState(loading = true)
                }
                .catch {
                    stateFlow.value = EditNoteUiState(errorResourceId = parseHttpError(it))
                }.collect {
                    stateFlow.value = it
                }
        }
    }
}