package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.AddNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class AddNoteViewModel(private val useCase: AddNoteUseCase) : ViewModel() {

    val stateFlow = MutableStateFlow(AddNoteUiState())

    fun addNote(title: String, description: String, pictureUrl: String?) {
        viewModelScope.launch {
            useCase.addNote(title, description, pictureUrl)
                .onStart {
                    stateFlow.value = AddNoteUiState(loading = true)
                }
                .catch {
                    stateFlow.value = AddNoteUiState(errorResourceId = parseHttpError(it))
                }.collect {
                    stateFlow.value = it
                }
        }
    }
}