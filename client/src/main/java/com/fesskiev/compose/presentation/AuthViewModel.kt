package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.LoginUseCase
import com.fesskiev.compose.domain.RegistrationUseCase
import com.fesskiev.compose.domain.Result
import com.fesskiev.compose.state.AuthUiState
import com.fesskiev.compose.state.AuthUserInputState
import com.fesskiev.compose.state.ErrorState
import com.fesskiev.compose.state.copyWithUserInputError
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val registrationUseCase: RegistrationUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    val uiStateFlow = MutableStateFlow(AuthUiState())

    fun registration(email: String, displayName: String, password: String) {
        viewModelScope.launch {
            uiStateFlow.apply {
                update { uiState ->
                    uiState.copy(
                        loading = true,
                        authUserInputState = AuthUserInputState(),
                        error = null
                    )
                }
                val result = registrationUseCase(email, displayName, password)
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                authUserInputState = AuthUserInputState(success = true),
                                error = null

                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                authUserInputState = AuthUserInputState().copyWithUserInputError(result.e),
                                error = ErrorState(errorResourceId = parseHttpError(result.e))
                            )
                        }
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            uiStateFlow.apply {
                update { uiState ->
                    uiState.copy(
                        loading = true,
                        authUserInputState = AuthUserInputState(),
                        error = null
                    )
                }
                val result = loginUseCase(email, password)
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                authUserInputState = AuthUserInputState(success = true),
                                error = null

                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                authUserInputState = AuthUserInputState().copyWithUserInputError(result.e),
                                error = ErrorState(errorResourceId = parseHttpError(result.e))
                            )
                        }
                    }
                }
            }
        }
    }

    fun toggleForm() {
        uiStateFlow.update {
            it.copy(
                loading = false,
                displayName = "",
                email = "",
                password = "",
                isLoginFormShow = !it.isLoginFormShow,
                authUserInputState = AuthUserInputState(),
                error= null
            )
        }
    }

    fun changeDisplayName(displayName: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                displayName = displayName,
                authUserInputState = AuthUserInputState(),
                error = null
            )
        }
    }

    fun changeEmail(email: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                email = email,
                authUserInputState = AuthUserInputState(),
                error = null
            )
        }
    }

    fun changePassword(password: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                password = password,
                authUserInputState = AuthUserInputState(),
                error = null
            )
        }
    }
}