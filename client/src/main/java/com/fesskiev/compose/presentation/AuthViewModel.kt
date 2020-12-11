package com.fesskiev.compose.presentation

import androidx.lifecycle.MutableLiveData
import com.fesskiev.compose.domain.LoginUseCase
import com.fesskiev.compose.domain.RegistrationUseCase
import com.fesskiev.compose.ui.screens.auth.AuthUiState

class AuthViewModel(private val registrationUseCase: RegistrationUseCase, private val loginUseCase: LoginUseCase) : BaseViewModel() {

    val liveData: MutableLiveData<AuthUiState> = MutableLiveData()

    init {
        liveData.postValue(AuthUiState.Empty)
    }

    fun registration(email: String, displayName: String, password: String) {
        liveData.postValue(AuthUiState.Loading)
        launchDataLoad(load = {
            val authUiState = registrationUseCase.registration(email, displayName, password)
            liveData.postValue(authUiState)
        }, error = {
            liveData.postValue(AuthUiState.Error(it))
        })
    }

    fun login(email: String, password: String) {
        liveData.postValue(AuthUiState.Loading)
        launchDataLoad(load = {
            val authUiState = loginUseCase.login(email, password)
            liveData.postValue(authUiState)
        }, error = {
            liveData.postValue(AuthUiState.Error(it))
        })
    }
}