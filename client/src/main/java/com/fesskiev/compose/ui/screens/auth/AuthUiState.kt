package com.fesskiev.compose.ui.screens.auth

import androidx.annotation.StringRes

data class AuthUiState(
    val isAuthSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isEmptyPasswordError: Boolean = false,
    val isEmptyEmailError: Boolean = false,
    val isEmptyDisplayNameError: Boolean = false,
    val isValidateEmailError: Boolean = false,
    val isValidatePasswordError: Boolean = false,
    @StringRes
    val errorResourceId: Int? = null
)