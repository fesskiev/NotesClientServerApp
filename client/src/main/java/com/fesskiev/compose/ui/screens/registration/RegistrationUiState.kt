package com.fesskiev.compose.ui.screens.registration

data class RegistrationUiState(
    val isRegistrationSuccess: Boolean = false,
    val isLoading: Boolean = false,
    val isEmptyPasswordError: Boolean = false,
    val isEmptyEmailError: Boolean = false,
    val isEmptyDisplayNameError: Boolean = false,
    val isValidateEmailError: Boolean = false,
    val isValidatePasswordError: Boolean = false,
    val errorMessage: String? = null
)