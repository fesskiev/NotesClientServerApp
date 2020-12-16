package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.LoginUseCase
import com.fesskiev.compose.domain.RegistrationUseCase
import com.fesskiev.compose.ui.screens.auth.AuthUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AuthViewModel(
    private val registrationUseCase: RegistrationUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    val stateFlow = MutableStateFlow(AuthUiState())

    fun registration(email: String, displayName: String, password: String) {
        viewModelScope.launch {
            registrationUseCase.registration(email, displayName, password)
                .onStart {
                    stateFlow.value = AuthUiState(loading = true)
                }
                .catch {
                    stateFlow.value = AuthUiState(errorResourceId = parseHttpError(it))
                }.collect {
                    stateFlow.value = it
                }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase.login(email, password)
                .onStart {
                    stateFlow.value = AuthUiState(loading = true)
                }
                .catch {
                    stateFlow.value = AuthUiState(errorResourceId = parseHttpError(it))
                }.collect {
                    stateFlow.value = it
                }
        }
    }
}