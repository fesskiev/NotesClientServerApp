package com.fesskiev.compose.ui.screens.auth

import androidx.annotation.StringRes
import com.fesskiev.compose.R

data class AuthUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val isEmptyPasswordError: Boolean = false,
    val isEmptyEmailError: Boolean = false,
    val isEmptyDisplayNameError: Boolean = false,
    val isValidateEmailError: Boolean = false,
    val isValidatePasswordError: Boolean = false,
    @StringRes
    val errorResourceId: Int? = null,
)

