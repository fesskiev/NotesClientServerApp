package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.ui.screens.auth.AuthUiState
import com.fesskiev.compose.ui.utils.FieldValidator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUseCase(private val repository: Repository, private val validator: FieldValidator) {

    suspend fun login(email: String, password: String): Flow<AuthUiState> = flow {
        if (validator.emptyEmail(email)) {
            return@flow emit(AuthUiState(isEmptyEmailError = true))
        }
        if (validator.emptyPassword(password)) {
            return@flow emit(AuthUiState(isEmptyPasswordError = true))
        }
        if (!validator.validateEmail(email)) {
            return@flow emit(AuthUiState(isValidateEmailError = true))
        }
        if (!validator.validatePassword(password)) {
            return@flow emit(AuthUiState(isValidatePasswordError = true))
        }
        repository.login(email, password)
        emit(AuthUiState(success = true))
    }
}