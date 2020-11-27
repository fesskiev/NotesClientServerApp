package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.auth.AuthUiState
import com.fesskiev.compose.ui.utils.FieldValidator

class RegistrationUseCase(private val repository: Repository, private val validator: FieldValidator) {

    suspend fun registration(email: String, displayName: String, password: String): AuthUiState  {
        if (validator.emptyDisplayName(displayName)) {
            return AuthUiState.ValidationError(isEmptyDisplayNameError = true)
        }
        if (validator.emptyEmail(email)) {
            return AuthUiState.ValidationError(isEmptyEmailError = true)
        }
        if (validator.emptyPassword(password)) {
            return AuthUiState.ValidationError(isEmptyPasswordError = true)
        }
        if (!validator.validateEmail(email)) {
            return AuthUiState.ValidationError(isValidateEmailError = true)
        }
        if (!validator.validatePassword(password)) {
            return AuthUiState.ValidationError(isValidatePasswordError = true)
        }
        repository.registration(email, displayName, password)
        return AuthUiState.Success
    }
}