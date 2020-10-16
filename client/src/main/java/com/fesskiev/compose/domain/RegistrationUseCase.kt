package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.registration.RegistrationUiState
import com.fesskiev.compose.ui.utils.FieldValidator

class RegistrationUseCase(private val repository: Repository, private val validator: FieldValidator) {

    suspend fun registration(email: String, displayName: String, password: String): RegistrationUiState  {
        if (validator.emptyDisplayName(displayName)) {
            return RegistrationUiState(isEmptyDisplayNameError = true)
        }
        if (validator.emptyEmail(email)) {
            return RegistrationUiState(isEmptyEmailError = true)
        }
        if (validator.emptyPassword(password)) {
            return RegistrationUiState(isEmptyPasswordError = true)
        }
        if (!validator.validateEmail(email)) {
            return RegistrationUiState(isValidateEmailError = true)
        }
        if (!validator.validatePassword(password)) {
            return RegistrationUiState(isValidatePasswordError = true)
        }
        repository.registration(email, displayName, password)
        return RegistrationUiState(isRegistrationSuccess = true)
    }
}