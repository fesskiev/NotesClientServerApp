package com.fesskiev.compose.presentation

import androidx.compose.runtime.mutableStateOf
import com.fesskiev.ServerErrorCodes.DISPLAY_NAME_EMPTY
import com.fesskiev.ServerErrorCodes.EMAIL_EMPTY
import com.fesskiev.ServerErrorCodes.EMAIL_INVALID
import com.fesskiev.ServerErrorCodes.PASSWORD_EMPTY
import com.fesskiev.ServerErrorCodes.PASSWORD_INVALID
import com.fesskiev.compose.data.remote.parseHttpError
import com.fesskiev.compose.domain.LoginUseCase
import com.fesskiev.compose.domain.RegistrationUseCase
import com.fesskiev.compose.domain.Result
import com.fesskiev.compose.domain.exceptions.UserInputException
import com.fesskiev.compose.state.AuthUiState
import com.fesskiev.compose.state.AuthUserInputState
import com.fesskiev.compose.state.ErrorState
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
                                authSuccess = true,
                                loading = false,
                                error = null

                            )
                        }
                        is Result.Failure -> {
                            if (result.e is UserInputException) {
                                uiState.copy(
                                    loading = false,
                                    authUserInputState = parseUserInputException(result.e),
                                    error = null
                                )
                            } else {
                                uiState.copy(
                                    loading = false,
                                    error = ErrorState(errorResourceId = parseHttpError(result.e))
                                )
                            }
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
                        error = null
                    )
                }
                val result = loginUseCase(email, password)
                update { uiState ->
                    when (result) {
                        is Result.Success -> {
                            uiState.copy(
                                authSuccess = true,
                                loading = false,
                                error = null
                            )
                        }
                        is Result.Failure -> {
                            if (result.e is UserInputException) {
                                uiState.copy(
                                    loading = false,
                                    authUserInputState = parseUserInputException(result.e),
                                    error = null
                                )
                            } else {
                                uiState.copy(
                                    loading = false,
                                    error = ErrorState(errorResourceId = parseHttpError(result.e))
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun parseUserInputException(e: UserInputException): AuthUserInputState =
        when (e.errorCode) {
            PASSWORD_EMPTY -> AuthUserInputState(isEmptyPasswordError = true)
            EMAIL_EMPTY -> AuthUserInputState(isEmptyEmailError = true)
            DISPLAY_NAME_EMPTY -> AuthUserInputState(isEmptyDisplayNameError = true)
            EMAIL_INVALID -> AuthUserInputState(isValidateEmailError = true)
            PASSWORD_INVALID -> AuthUserInputState(isValidatePasswordError = true)
            else -> AuthUserInputState()
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
                error = null
            )
        }
    }

    fun changeDisplayName(displayName: String) {
        authUiState.update { it.copy(displayName = displayName) }
    }

    fun changeEmail(email: String) {
        authUiState.update {
            it.copy(
                email = email,
                authUserInputState = AuthUserInputState()
            )
        }
    }

    fun changePassword(password: String) {
        authUiState.update {
            it.copy(
                password = password,
                authUserInputState = AuthUserInputState()
            )
        }
    }
}