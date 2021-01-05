package com.fesskiev.compose.domain

import com.fesskiev.compose.data.Repository
import com.fesskiev.compose.presentation.AuthState
import com.fesskiev.compose.ui.utils.FieldValidator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginUseCase(private val repository: Repository, private val validator: FieldValidator) {

    fun login(email: String, password: String): Flow<AuthState> = flow {
        if (validator.emptyEmail(email)) {
            return@flow emit(AuthState(isEmptyEmailError = true))
        }
        if (validator.emptyPassword(password)) {
            return@flow emit(AuthState(isEmptyPasswordError = true))
        }
        if (!validator.validateEmail(email)) {
            return@flow emit(AuthState(isValidateEmailError = true))
        }
        if (!validator.validatePassword(password)) {
            return@flow emit(AuthState(isValidatePasswordError = true))
        }
        repository.login(email, password)
        emit(AuthState(success = true))
    }
}