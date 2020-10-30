package com.fesskiev.compose.ui.screens.auth

data class AuthUiState(
    val isAuthSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isEmptyPasswordError: Boolean = false,
    val isEmptyEmailError: Boolean = false,
    val isEmptyDisplayNameError: Boolean = false,
    val isValidateEmailError: Boolean = false,
    val isValidatePasswordError: Boolean = false,
    val errorMessage: String? = null
)