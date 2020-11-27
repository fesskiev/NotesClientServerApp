package com.fesskiev.compose.ui.screens.auth

import androidx.annotation.StringRes

sealed class AuthUiState {
    object Init : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(@StringRes val errorResourceId: Int) : AuthUiState()
    data class ValidationError(
        val isEmptyPasswordError: Boolean = false,
        val isEmptyEmailError: Boolean = false,
        val isEmptyDisplayNameError: Boolean = false,
        val isValidateEmailError: Boolean = false,
        val isValidatePasswordError: Boolean = false,
    ) : AuthUiState()
}