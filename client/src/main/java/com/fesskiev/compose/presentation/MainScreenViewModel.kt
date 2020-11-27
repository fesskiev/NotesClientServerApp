package com.fesskiev.compose.presentation

import androidx.lifecycle.MutableLiveData
import com.fesskiev.compose.domain.NotesUseCase
import com.fesskiev.compose.ui.screens.main.MainUiState

class MainScreenViewModel(private val useCase: NotesUseCase) : BaseViewModel() {

    val liveData: MutableLiveData<MainUiState> = MutableLiveData()

    init {
        liveData.postValue(MainUiState.Loading)
    }

    fun getNotes() {
        launchDataLoad(load = {
            val result = useCase.getNotes()
            liveData.postValue(result)
        }, error = {
            liveData.postValue(MainUiState.Error(it))
        })
    }
}