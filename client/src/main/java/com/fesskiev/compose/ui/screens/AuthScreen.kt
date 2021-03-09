package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.fesskiev.compose.R
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import com.fesskiev.compose.presentation.AuthViewModel
import com.fesskiev.compose.ui.components.*
import org.koin.androidx.compose.getViewModel

@Composable
fun AuthScreen(navController: NavController, viewModel: AuthViewModel = getViewModel()) {
    var displayName by rememberSaveable { mutableStateOf("test") }
    var email by rememberSaveable { mutableStateOf("test@i.ua") }
    var password by rememberSaveable { mutableStateOf("123456") }
    var isLoginForm by rememberSaveable { mutableStateOf(true) }

    val uiState = viewModel.stateFlow.collectAsState().value
    when {
        uiState.authState.success -> navController.navigate("main")
        uiState.loading -> ProgressBar()
        else -> {
            var isErrorEmail =
                uiState.authState.isEmptyEmailError || uiState.authState.isValidateEmailError
            var isErrorPassword =
                uiState.authState.isEmptyPasswordError || uiState.authState.isValidatePasswordError
            var isErrorDisplayName = uiState.authState.isEmptyDisplayNameError
            val emailLabel = when {
                uiState.authState.isEmptyEmailError -> stringResource(R.string.error_empty_email)
                uiState.authState.isValidateEmailError -> stringResource(R.string.error_validate_email)
                else -> stringResource(R.string.email)
            }
            val passwordLabel = when {
                uiState.authState.isEmptyPasswordError -> stringResource(R.string.error_empty_password)
                uiState.authState.isValidatePasswordError -> stringResource(R.string.error_validate_password)
                else -> stringResource(R.string.password)
            }
            val displayNameLabel = stringResource(R.string.error_empty_display_name)

            AppScaffold(
                content = {
                    AuthForm(
                        displayName = displayName,
                        email = email,
                        password = password,
                        isLoginForm = isLoginForm,
                        emailLabel = emailLabel,
                        passwordLabel = passwordLabel,
                        displayNameLabel = displayNameLabel,
                        isErrorEmail = isErrorEmail,
                        isErrorPassword = isErrorPassword,
                        isErrorDisplayName = isErrorDisplayName,
                        registrationOnClick = {
                            viewModel.registration(email, displayName, password)
                        },
                        loginOnClick = {
                            viewModel.login(email, password)
                        },
                        toggleFormOnClick = { isLoginForm = !isLoginForm },
                        displayNameOnChange = { displayName = it },
                        emailOnChange = { email = it },
                        passwordOnChange = { password = it }
                    )
                },
                errorResourceId = uiState.errorResourceId
            )
        }
    }
}

@Composable
fun AuthForm(
    displayName: String,
    email: String,
    password: String,
    isLoginForm: Boolean,
    emailLabel: String,
    passwordLabel: String,
    displayNameLabel: String,
    isErrorEmail: Boolean,
    isErrorPassword: Boolean,
    isErrorDisplayName: Boolean,
    registrationOnClick: () -> Unit,
    loginOnClick: () -> Unit,
    toggleFormOnClick: () -> Unit,
    displayNameOnChange: (String) -> Unit,
    emailOnChange: (String) -> Unit,
    passwordOnChange: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when {
                isLoginForm -> stringResource(R.string.login)
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
            if (!isLoginForm) {
                onClick = registrationOnClick
                AsciiTextField(
                    label = displayNameLabel,
                    value = displayName,
                    onValueChange = displayNameOnChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    isError = isErrorDisplayName
                )
            }
            EmailTextField(
                label = emailLabel,
                value = email,
                onValueChange = emailOnChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                isError = isErrorEmail,
            )
            PasswordTextField(
                label = passwordLabel,
                value = password,
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
                isLoginForm -> stringResource(R.string.registration)
                else -> stringResource(R.string.login)
            },
            textDecoration = TextDecoration.Underline
        )
    }
}