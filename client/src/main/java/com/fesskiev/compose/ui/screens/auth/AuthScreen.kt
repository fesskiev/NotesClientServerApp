package com.fesskiev.compose.ui.screens.auth

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.navigate
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.AuthViewModel
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.components.SnackBar
import org.koin.androidx.compose.getViewModel

@Composable
fun AuthScreen(navController: NavController, viewModel: AuthViewModel = getViewModel()) {
    val displayName = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val email = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val password = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    var isLoginForm = savedInstanceState { true }

    viewModel.liveData.observeAsState().value?.let { uiState ->
        when {
            uiState.isAuthSuccess -> navController.navigate("main")
            uiState.isLoading -> ProgressBar()
            else -> {
                AuthForm(
                    displayNameState = displayName,
                    emailState = email,
                    passwordState = password,
                    isLoginForm = isLoginForm,
                    uiState = uiState,
                    registrationOnClick = {
                        viewModel.registration(
                            email = email.value.text,
                            displayName = displayName.value.text,
                            password = password.value.text
                        )
                    },
                    loginOnClick = {
                        viewModel.login(
                            email = email.value.text,
                            password = password.value.text
                        )
                    }
                )
                uiState.errorMessage?.let { message ->
                    SnackBar(message)
                }
            }
        }
    }
}

@Composable
fun AuthForm(
    displayNameState: MutableState<TextFieldValue>,
    emailState: MutableState<TextFieldValue>,
    passwordState: MutableState<TextFieldValue>,
    isLoginForm: MutableState<Boolean>,
    uiState: AuthUiState,
    registrationOnClick: () -> Unit,
    loginOnClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = when {
                isLoginForm.value -> stringResource(R.string.login)
                else -> stringResource(R.string.registration)
            },
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        if (isLoginForm.value) {
            LoginForm(
                emailState = emailState,
                passwordState = passwordState,
                uiState = uiState,
                loginOnClick = loginOnClick
            )
        } else {
            RegistrationForm(
                displayNameState = displayNameState,
                emailState = emailState,
                passwordState = passwordState,
                uiState = uiState,
                registrationOnClick = registrationOnClick
            )
        }
        Text(
            modifier = Modifier.padding(top = 8.dp).clickable(
                onClick = {
                    isLoginForm.value = !isLoginForm.value
                },
            ),
            text = when {
                isLoginForm.value -> stringResource(R.string.registration)
                else -> stringResource(R.string.login)
            },
            textDecoration = TextDecoration.Underline
        )
    }
}

@Composable
fun LoginForm(
    emailState: MutableState<TextFieldValue>,
    passwordState: MutableState<TextFieldValue>,
    uiState: AuthUiState,
    loginOnClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = {
                Text(
                    text = when {
                        uiState.isEmptyEmailError -> stringResource(R.string.error_empty_email)
                        uiState.isValidateEmailError -> stringResource(R.string.error_validate_email)
                        else -> stringResource(R.string.email)
                    }
                )
            },
            keyboardType = KeyboardType.Email,
            isErrorValue = uiState.isEmptyEmailError || uiState.isValidateEmailError
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = {
                Text(
                    text = when {
                        uiState.isEmptyPasswordError -> stringResource(R.string.error_empty_password)
                        uiState.isValidatePasswordError -> stringResource(R.string.error_validate_password)
                        else -> stringResource(R.string.password)
                    }
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password,
            isErrorValue = uiState.isEmptyPasswordError || uiState.isValidatePasswordError,
        )
        Button(
            modifier = Modifier.padding(top = 8.dp).height(48.dp).align(Alignment.End),
            onClick = loginOnClick
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}

@Composable
fun RegistrationForm(
    displayNameState: MutableState<TextFieldValue>,
    emailState: MutableState<TextFieldValue>,
    passwordState: MutableState<TextFieldValue>,
    uiState: AuthUiState,
    registrationOnClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            value = displayNameState.value,
            onValueChange = { displayNameState.value = it },
            label = {
                Text(
                    text = when {
                        uiState.isEmptyDisplayNameError -> stringResource(R.string.error_empty_display_name)
                        else -> stringResource(R.string.display_name)
                    }
                )
            },
            isErrorValue = uiState.isEmptyDisplayNameError
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = {
                Text(
                    text = when {
                        uiState.isEmptyEmailError -> stringResource(R.string.error_empty_email)
                        uiState.isValidateEmailError -> stringResource(R.string.error_validate_email)
                        else -> stringResource(R.string.email)
                    }
                )
            },
            keyboardType = KeyboardType.Email,
            isErrorValue = uiState.isEmptyEmailError || uiState.isValidateEmailError
        )
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = {
                Text(
                    text = when {
                        uiState.isEmptyPasswordError -> stringResource(R.string.error_empty_password)
                        uiState.isValidatePasswordError -> stringResource(R.string.error_validate_password)
                        else -> stringResource(R.string.password)
                    }
                )
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password,
            isErrorValue = uiState.isEmptyPasswordError || uiState.isValidatePasswordError,
        )
        Button(
            modifier = Modifier.padding(top = 8.dp).height(48.dp).align(Alignment.End),
            onClick = registrationOnClick
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}