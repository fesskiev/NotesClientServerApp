package com.fesskiev.compose.ui.screens.registration

import androidx.compose.foundation.Text
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
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.fesskiev.compose.R
import com.fesskiev.compose.presentation.RegistrationViewModel
import com.fesskiev.compose.ui.components.ProgressBar
import com.fesskiev.compose.ui.components.SnackBar
import com.fesskiev.compose.ui.utils.AppTheme
import org.koin.androidx.compose.getViewModel

@Composable
fun RegistrationScreen(viewModel: RegistrationViewModel = getViewModel()) {
    val displayName = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val email = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    val password = savedInstanceState(saver = TextFieldValue.Saver) { TextFieldValue() }
    viewModel.liveData.observeAsState().value?.let { uiState ->
        if (uiState.isLoading) {
            ProgressBar()
        } else {
            Registration(
                displayNameState = displayName,
                emailState = email,
                passwordState = password,
                uiState = uiState,
                submitOnClick = {
                    viewModel.registration(
                        email = email.value.text,
                        displayName = displayName.value.text,
                        password = password.value.text
                    )
                })
            uiState.errorMessage?.let { message ->
                SnackBar(message)
            }
        }
    }
}

@Composable
fun Registration(
    displayNameState: MutableState<TextFieldValue>,
    emailState: MutableState<TextFieldValue>,
    passwordState: MutableState<TextFieldValue>,
    uiState: RegistrationUiState,
    submitOnClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            stringResource(R.string.registration),
            modifier = Modifier.padding(top = 16.dp, bottom = 16.dp)
        )
        val labelDisplayName = when {
            uiState.isEmptyDisplayNameError -> stringResource(R.string.error_empty_display_name)
            else -> stringResource(R.string.display_name)
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            value = displayNameState.value,
            onValueChange = { displayNameState.value = it },
            label = { Text(labelDisplayName) },
            isErrorValue = uiState.isEmptyDisplayNameError
        )
        val labelEmail = when {
            uiState.isEmptyEmailError -> stringResource(R.string.error_empty_email)
            uiState.isValidateEmailError -> stringResource(R.string.error_validate_email)
            else -> stringResource(R.string.email)
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text(labelEmail) },
            keyboardType = KeyboardType.Email,
            isErrorValue = uiState.isEmptyEmailError || uiState.isValidateEmailError
        )
        val labelPassword = when {
            uiState.isEmptyPasswordError-> stringResource(R.string.error_empty_password)
            uiState.isValidatePasswordError -> stringResource(R.string.error_validate_password)
            else -> stringResource(R.string.password)
        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text(labelPassword) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password,
            isErrorValue = uiState.isEmptyPasswordError || uiState.isValidatePasswordError,
        )
        Button(
            modifier = Modifier.padding(top = 8.dp).height(48.dp).align(Alignment.End),
            onClick = submitOnClick
        ) {
            Text(stringResource(R.string.submit))
        }
    }
}

@Preview("Registration")
@Composable
private fun registrationPreview() {
    AppTheme {
        RegistrationScreen()
    }
}