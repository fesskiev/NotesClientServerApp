package com.fesskiev.compose.presentation

import androidx.compose.runtime.mutableStateOf
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.LoginUseCase
import com.fesskiev.compose.domain.RegistrationUseCase
import com.fesskiev.compose.domain.Result
import com.fesskiev.compose.state.AuthUiState
import com.fesskiev.compose.state.AuthUserInputState
import com.fesskiev.compose.state.ErrorState
import com.fesskiev.compose.state.copyWithUserInputError
import com.fesskiev.compose.ui.utils.update
import kotlinx.coroutines.launch

class AuthPresenter(
    private val registrationUseCase: RegistrationUseCase,
    private val loginUseCase: LoginUseCase
) : Presenter() {

    val authUiState = mutableStateOf(AuthUiState())

    fun registration(email: String, displayName: String, password: String) {
        coroutineScope.launch {
            authUiState.apply {
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
        coroutineScope.launch {
            authUiState.apply {
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
        authUiState.update {
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
        authUiState.update {
            it.copy(
                loading = false,
                displayName = displayName,
                authUserInputState = AuthUserInputState(),
                error = null
            )
        }
    }

    fun changeEmail(email: String) {
        authUiState.update {
            it.copy(
                loading = false,
                email = email,
                authUserInputState = AuthUserInputState(),
                error = null
            )
        }
    }

    fun changePassword(password: String) {
        authUiState.update {
            it.copy(
                loading = false,
                password = password,
                authUserInputState = AuthUserInputState(),
                error = null
            )
        }
    }
}