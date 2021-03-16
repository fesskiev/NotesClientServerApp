package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.AddNoteUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class AddNoteViewModel(private val useCase: AddNoteUseCase) : ViewModel() {

    val stateFlow = MutableStateFlow(AddNoteUiState())

    fun addNote(title: String, description: String, file: File?) {
        viewModelScope.launch {
            useCase.addNote(title, description, file)
                .onStart {
                    stateFlow.value = stateFlow.value.copy(
                        loading = true,
                        addNoteState = AddNoteState.Default,
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
                        addNoteState = it,
                        errorResourceId = null
                    )
                }
        }
    }

    fun changeTitle(title: String) {
        stateFlow.value = stateFlow.value.copy(
            loading = false,
            title = title,
            addNoteState = AddNoteState.Default,
            errorResourceId = null
        )
    }

    fun changeDescription(description: String) {
        stateFlow.value = stateFlow.value.copy(
            loading = false,
            description = description,
            addNoteState = AddNoteState.Default,
            errorResourceId = null
        )
    }
}