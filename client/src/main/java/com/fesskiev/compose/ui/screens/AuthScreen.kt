package com.fesskiev.compose.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.AuthViewModel
import com.fesskiev.compose.ui.components.*
import org.koin.androidx.compose.getViewModel

@Composable
fun AuthScreen(navController: NavController, viewModel: AuthViewModel = getViewModel()) {
    val displayNameState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val emailState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue("test1@i.ua") }
    val passwordState = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue("123456") }
    var isLoginFormState = savedInstanceState { true }

    val uiState = viewModel.stateFlow.collectAsState().value
    when {
        uiState.success -> navController.navigate("main")
        uiState.loading -> ProgressBar()
        else -> {
            var isErrorEmailValue = uiState.isEmptyEmailError || uiState.isValidateEmailError
            var isErrorPasswordValue =
                uiState.isEmptyPasswordError || uiState.isValidatePasswordError
            var isErrorDisplayNameValue = uiState.isEmptyDisplayNameError
            val emailLabel = when {
                uiState.isEmptyEmailError -> stringResource(R.string.error_empty_email)
                uiState.isValidateEmailError -> stringResource(R.string.error_validate_email)
                else -> stringResource(R.string.email)
            }
            val passwordLabel = when {
                uiState.isEmptyPasswordError -> stringResource(R.string.error_empty_password)
                uiState.isValidatePasswordError -> stringResource(R.string.error_validate_password)
                else -> stringResource(R.string.password)
            }
            val displayNameLabel = stringResource(R.string.error_empty_display_name)

            AuthForm(
                displayNameState = displayNameState,
                emailState = emailState,
                passwordState = passwordState,
                isLoginFormState = isLoginFormState,
                emailLabel = emailLabel,
                passwordLabel = passwordLabel,
                displayNameLabel = displayNameLabel,
                isErrorEmailValue = isErrorEmailValue,
                isErrorPasswordValue = isErrorPasswordValue,
                isErrorDisplayNameValue = isErrorDisplayNameValue,
                registrationOnClick = {
                    viewModel.registration(
                        email = emailState.value.text,
                        displayName = displayNameState.value.text,
                        password = passwordState.value.text
                    )
                },
                loginOnClick = {
                    viewModel.login(
                        email = emailState.value.text,
                        password = passwordState.value.text
                    )
                }
            )
            uiState.errorResourceId?.let {
                SnackBar(stringResource(it))
            }
        }
    }
}

@Composable
fun AuthForm(
    displayNameState: MutableState<TextFieldValue>,
    emailState: MutableState<TextFieldValue>,
    passwordState: MutableState<TextFieldValue>,
    isLoginFormState: MutableState<Boolean>,
    emailLabel: String,
    passwordLabel: String,
    displayNameLabel: String,
    isErrorEmailValue: Boolean,
    isErrorPasswordValue: Boolean,
    isErrorDisplayNameValue: Boolean,
    registrationOnClick: () -> Unit,
    loginOnClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when {
                isLoginFormState.value -> stringResource(R.string.login)
                else -> stringResource(R.string.registration)
            },
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
            style = MaterialTheme.typography.h5
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var onClick = loginOnClick
            if (!isLoginFormState.value) {
                onClick = registrationOnClick
                AsciiTextField(
                    label = displayNameLabel,
                    textFieldState = displayNameState,
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    isErrorValue = isErrorDisplayNameValue
                )
            }
            EmailTextField(
                label = emailLabel,
                textFieldState = emailState,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                isErrorValue = isErrorEmailValue,
            )
            PasswordTextField(
                label = passwordLabel,
                textFieldState = passwordState,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                isErrorValue = isErrorPasswordValue
            )
            Button(
                modifier = Modifier.padding(top = 8.dp).height(48.dp).align(Alignment.End),
                onClick = onClick
            ) {
                Text(stringResource(R.string.submit))
            }
        }
        Text(
            modifier = Modifier.padding(top = 8.dp).clickable(
                onClick = {
                    isLoginFormState.value = !isLoginFormState.value
                },
            ),
            style = MaterialTheme.typography.h6,
            text = when {
                isLoginFormState.value -> stringResource(R.string.registration)
                else -> stringResource(R.string.login)
            },
            textDecoration = TextDecoration.Underline
        )
    }
}