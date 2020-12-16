package com.fesskiev.compose.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
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
    val email = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue("test1@i.ua") }
    val password = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue("123456") }
    var isLoginForm = savedInstanceState { true }

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
                displayNameState = displayName,
                emailState = email,
                passwordState = password,
                isLoginForm = isLoginForm,
                emailLabel = emailLabel,
                passwordLabel = passwordLabel,
                displayNameLabel = displayNameLabel,
                isErrorEmailValue = isErrorEmailValue,
                isErrorPasswordValue = isErrorPasswordValue,
                isErrorDisplayNameValue = isErrorDisplayNameValue,
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
    isLoginForm: MutableState<Boolean>,
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
                isLoginForm.value -> stringResource(R.string.login)
                else -> stringResource(R.string.registration)
            },
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var onClick = loginOnClick
            if (!isLoginForm.value) {
                onClick = registrationOnClick
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    value = displayNameState.value,
                    onValueChange = { displayNameState.value = it },
                    label = { Text(displayNameLabel) },
                    isErrorValue = isErrorDisplayNameValue
                )
            }
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                value = emailState.value,
                onValueChange = { emailState.value = it },
                label = { Text(emailLabel) },
                isErrorValue = isErrorEmailValue,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                label = { Text(passwordLabel) },
                isErrorValue = isErrorPasswordValue,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
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