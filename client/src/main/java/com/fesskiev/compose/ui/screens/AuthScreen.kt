package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.AuthViewModel
import com.fesskiev.compose.state.AuthUiState
import com.fesskiev.compose.ui.components.*
import org.koin.androidx.compose.getViewModel

@Composable
fun AuthScreen(viewModel: AuthViewModel = getViewModel(), onCloseAppClick : () -> Unit, authSuccess: () -> Unit) {
    val uiState = viewModel.uiStateFlow.collectAsState().value
    when {
        uiState.authUserInputState.success -> LaunchedEffect(Unit) { authSuccess() }
        uiState.loading -> ProgressBar()
        else -> {
            AppScaffold(
                onBackPressed = onCloseAppClick,
                content = {
                    AuthForm(
                        uiState,
                        registrationOnClick = {
                            viewModel.registration(
                                uiState.email,
                                uiState.displayName,
                                uiState.password
                            )
                        },
                        loginOnClick = {
                            viewModel.login(uiState.email, uiState.password)
                        },
                        toggleFormOnClick = { viewModel.toggleForm() },
                        displayNameOnChange = { viewModel.changeDisplayName(it) },
                        emailOnChange = { viewModel.changeEmail(it) }
                    ) { viewModel.changePassword(it) }
                },
                error = uiState.error
            )
        }
    }
}

@Composable
fun AuthForm(
    uiState: AuthUiState,
    registrationOnClick: () -> Unit,
    loginOnClick: () -> Unit,
    toggleFormOnClick: () -> Unit,
    displayNameOnChange: (String) -> Unit,
    emailOnChange: (String) -> Unit,
    passwordOnChange: (String) -> Unit,
) {
    val isErrorEmail =
        uiState.authUserInputState.isEmptyEmailError || uiState.authUserInputState.isValidateEmailError
    val isErrorPassword =
        uiState.authUserInputState.isEmptyPasswordError || uiState.authUserInputState.isValidatePasswordError
    val isErrorDisplayName = uiState.authUserInputState.isEmptyDisplayNameError
    val emailLabel = when {
        uiState.authUserInputState.isEmptyEmailError -> stringResource(R.string.error_empty_email)
        uiState.authUserInputState.isValidateEmailError -> stringResource(R.string.error_validate_email)
        else -> stringResource(R.string.email)
    }
    val passwordLabel = when {
        uiState.authUserInputState.isEmptyPasswordError -> stringResource(R.string.error_empty_password)
        uiState.authUserInputState.isValidatePasswordError -> stringResource(R.string.error_validate_password)
        else -> stringResource(R.string.password)
    }
    val displayNameLabel = when {
        uiState.authUserInputState.isEmptyDisplayNameError -> stringResource(R.string.error_empty_display_name)
        else -> stringResource(R.string.display_name)
    }

    val isLoginFormShow = uiState.isLoginFormShow

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when {
                isLoginFormShow -> stringResource(R.string.login)
                else -> stringResource(R.string.registration)
            },
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            style = MaterialTheme.typography.h5
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var onClick = loginOnClick
            if (!isLoginFormShow) {
                onClick = registrationOnClick
                AsciiOutlinedTextField(
                    label = displayNameLabel,
                    value = uiState.displayName,
                    onValueChange = displayNameOnChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    isError = isErrorDisplayName
                )
            }
            EmailOutlinedTextField(
                label = emailLabel,
                value = uiState.email,
                onValueChange = emailOnChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                isError = isErrorEmail,
            )
            PasswordOutlinedTextField(
                label = passwordLabel,
                value = uiState.password,
                onValueChange = passwordOnChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                isError = isErrorPassword
            )
            Button(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(48.dp)
                    .align(Alignment.End),
                onClick = onClick
            ) {
                Text(stringResource(R.string.submit))
            }
        }
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable(onClick = toggleFormOnClick),
            style = MaterialTheme.typography.h6,
            text = when {
                isLoginFormShow -> stringResource(R.string.registration)
                else -> stringResource(R.string.login)
            },
            textDecoration = TextDecoration.Underline
        )
    }
}