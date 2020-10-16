package com.fesskiev.compose.presentation

import androidx.lifecycle.MutableLiveData
import com.fesskiev.compose.domain.RegistrationUseCase
import com.fesskiev.compose.ui.screens.registration.RegistrationUiState

class RegistrationViewModel(private val useCase: RegistrationUseCase) : BaseViewModel() {

    val liveData: MutableLiveData<RegistrationUiState> = MutableLiveData()

    init {
        liveData.postValue(RegistrationUiState())
    }

    fun registration(email: String, displayName: String, password: String) {
        liveData.postValue(RegistrationUiState(isLoading = true))
        launchDataLoad(load = {
            val result = useCase.registration(email, displayName, password)
            liveData.postValue(result)
        }, error = {
            liveData.postValue(RegistrationUiState(errorMessage = it))
        })
    }
}