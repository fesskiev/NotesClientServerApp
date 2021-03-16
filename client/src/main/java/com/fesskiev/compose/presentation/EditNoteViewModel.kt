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

    fun editNote(noteUid: Int, title: String, description: String) {
        viewModelScope.launch {
            editNoteUseCase.editNote(noteUid, title, description)
                .onStart {
                    stateFlow.value = stateFlow.value.copy(
                        loading = true,
                        editNoteState = EditNoteState.Default,
                        errorResourceId = null
                    )
                }
                .catch {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            errorResourceId = parseHttpError(it)
                        )
                }.collect {
                    stateFlow.value = stateFlow.value.copy(
                        loading = false,
                        editNoteState = it,
                        errorResourceId = null
                    )
                }
        }
    }

    fun getNoteByUid(noteUid: Int) {
        viewModelScope.launch {
            getNoteByIdUseCase.getNoteByUid(noteUid)
                .onStart {
                    stateFlow.value = stateFlow.value.copy(
                        loading = true,
                        title = "",
                        description = "",
                        editNoteState = EditNoteState.Default,
                        errorResourceId = null
                    )
                }
                .catch {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            errorResourceId = parseHttpError(it)
                        )
                }.collect {
                    stateFlow.value = stateFlow.value.copy(
                        loading = false,
                        title = it.title,
                        description = it.description,
                        errorResourceId = null
                    )
                }
        }
    }

    fun changeTitle(title: String) {
        stateFlow.value = stateFlow.value.copy(
            loading = false,
            title = title,
            editNoteState = EditNoteState.Default,
            errorResourceId = null
        )
    }

    fun changeDescription(description: String) {
        stateFlow.value = stateFlow.value.copy(
            loading = false,
            description = description,
            editNoteState = EditNoteState.Default,
            errorResourceId = null
        )
    }
}