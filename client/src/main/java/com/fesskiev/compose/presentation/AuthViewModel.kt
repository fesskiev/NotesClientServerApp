package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.LoginUseCase
import com.fesskiev.compose.domain.RegistrationUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
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
                    stateFlow.value = stateFlow.value.copy(
                        loading = true,
                        authState = AuthState(),
                        errorResourceId = null
                    )
                }
                .catch {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            authState = AuthState(),
                            errorResourceId = parseHttpError(it)
                        )
                }.collect {
                    stateFlow.value =
                        stateFlow.value.copy(loading = false, authState = it, errorResourceId = null)
                }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase.login(email, password)
                .onStart {
                    stateFlow.value = stateFlow.value.copy(
                        loading = true,
                        authState = AuthState(),
                        errorResourceId = null
                    )
                }
                .catch {
                    stateFlow.value =
                        stateFlow.value.copy(
                            loading = false,
                            authState = AuthState(),
                            errorResourceId = parseHttpError(it)
                        )
                }.collect {
                    stateFlow.value =
                        stateFlow.value.copy(loading = false, authState = it, errorResourceId = null)
                }
        }
    }
}