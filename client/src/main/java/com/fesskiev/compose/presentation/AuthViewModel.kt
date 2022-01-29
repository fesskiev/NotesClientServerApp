package com.fesskiev.compose.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.LoginUseCase
import com.fesskiev.compose.domain.RegistrationUseCase
import com.fesskiev.compose.domain.Result
import com.fesskiev.compose.mvi.AuthUiState
import com.fesskiev.compose.mvi.AuthUserInputState
import com.fesskiev.compose.mvi.copyWithUserInputError
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
                        errorResourceId = null
                    )
                }
                val result = registrationUseCase(email, displayName, password)
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                authUserInputState = AuthUserInputState(success = true),
                                errorResourceId = null

                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                authUserInputState = AuthUserInputState().copyWithUserInputError(result.e),
                                errorResourceId = parseHttpError(result.e)
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
                        errorResourceId = null
                    )
                }
                val result = loginUseCase(email, password)
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            uiState.copy(
                                loading = false,
                                authUserInputState = AuthUserInputState(success = true),
                                errorResourceId = null

                            )
                        }
                        is Result.Failure -> {
                            uiState.copy(
                                loading = false,
                                authUserInputState = AuthUserInputState().copyWithUserInputError(result.e),
                                errorResourceId = parseHttpError(result.e)
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
                errorResourceId = null
            )
        }
    }

    fun changeDisplayName(displayName: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                displayName = displayName,
                authUserInputState = AuthUserInputState(),
                errorResourceId = null
            )
        }
    }

    fun changeEmail(email: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                email = email,
                authUserInputState = AuthUserInputState(),
                errorResourceId = null
            )
        }
    }

    fun changePassword(password: String) {
        uiStateFlow.update {
            it.copy(
                loading = false,
                password = password,
                authUserInputState = AuthUserInputState(),
                errorResourceId = null
            )
        }
    }
}