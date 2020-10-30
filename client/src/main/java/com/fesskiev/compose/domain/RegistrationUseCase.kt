package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.auth.AuthUiState
import com.fesskiev.compose.ui.utils.FieldValidator

class RegistrationUseCase(private val repository: Repository, private val validator: FieldValidator) {

    suspend fun registration(email: String, displayName: String, password: String): AuthUiState  {
        if (validator.emptyDisplayName(displayName)) {
            return AuthUiState(isEmptyDisplayNameError = true)
        }
        if (validator.emptyEmail(email)) {
            return AuthUiState(isEmptyEmailError = true)
        }
        if (validator.emptyPassword(password)) {
            return AuthUiState(isEmptyPasswordError = true)
        }
        if (!validator.validateEmail(email)) {
            return AuthUiState(isValidateEmailError = true)
        }
        if (!validator.validatePassword(password)) {
            return AuthUiState(isValidatePasswordError = true)
        }
        repository.registration(email, displayName, password)
        return AuthUiState(isAuthSuccess = true)
    }
}